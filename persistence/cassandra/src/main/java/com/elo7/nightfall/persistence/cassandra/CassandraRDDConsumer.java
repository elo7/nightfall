package com.elo7.nightfall.persistence.cassandra;

import com.google.common.annotations.VisibleForTesting;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Helper class to handle RDD to be persisted into Cassandra.
 *
 * @param <T> Type of the JavaRDD.
 */
public class CassandraRDDConsumer<T> implements Serializable {

    private static final long serialVersionUID = 3L;
    private static final Logger LOGGER = LoggerFactory.getLogger(CassandraRDDPairConsumer.class);

    private final CassandraConfiguration configuration;

    @Inject
    public CassandraRDDConsumer(CassandraConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Consumes a JavaRDD applying the consumer function on the list of items of the RDD.
     * This method already take care of open and close the connection to the underline Cassandra.
     *
     * @param rdd      JavaRDD.
     * @param consumer Consumer function.
     */
    public void apply(JavaRDD<T> rdd, CassandraConsumer<T> consumer) {
        if (!rdd.isEmpty()) {

            VoidFunction<Iterator<T>> fn = (items) -> {
                if (!items.hasNext()) {
                    LOGGER.debug("Empty iterator, skipping!");
                    return;
                }

                LOGGER.info("Creating connection with Cassandra: {}.", configuration);
                try (CassandraConnection connection = CassandraConnectionFactory.create(configuration)) {
                    consumePartition(consumer, items, connection);
                }
            };

            if (configuration.isAsyncConsumerEnabled()) {
                rdd.foreachPartitionAsync(fn);
            } else {
                rdd.foreachPartition(fn);
            }

        }
    }

    @VisibleForTesting
    void consumePartition(CassandraConsumer<T> consumer, Iterator<T> items, CassandraConnection connection) {
        if (configuration.isBatchSplitEnabled()) {
            LOGGER.debug("Consuming partitions with split mode");
            consumeItemsSplitted(consumer, items, connection);
        } else {
            LOGGER.debug("Consuming partitions without split mode");
            consumer.consume(items, connection);
        }
    }

    @VisibleForTesting
    void consumeItemsSplitted(CassandraConsumer<T> consumer, Iterator<T> items, CassandraConnection connection) {
        int currentBatchSize = 0;
        List<T> itemList = new LinkedList<>();

        while (items.hasNext()) {
            itemList.add(items.next());
            currentBatchSize++;

            if (currentBatchSize >= configuration.getBatchSize()) {
                LOGGER.debug("Partition splitted into {} items to be consumed.", currentBatchSize);
                consumer.consume(itemList.iterator(), connection);
                itemList.clear();
                currentBatchSize = 0;
            }
        }

        if (!itemList.isEmpty()) {
            LOGGER.debug("Consuming remaining items[{}].", currentBatchSize);
            consumer.consume(itemList.iterator(), connection);
        }
    }
}
