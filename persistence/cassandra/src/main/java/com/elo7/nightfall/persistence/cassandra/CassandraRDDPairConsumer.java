package com.elo7.nightfall.persistence.cassandra;

import com.google.common.annotations.VisibleForTesting;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.VoidFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Helper class to handle RDD to be persisted into Cassandra.
 *
 * @param <K> Key type of the JavaPairRDD
 * @param <V> Value type of the JavaPairRDD
 */
public class CassandraRDDPairConsumer<K, V> implements Serializable {

    private static final long serialVersionUID = 3L;
    private static final Logger LOGGER = LoggerFactory.getLogger(CassandraRDDPairConsumer.class);
    private final CassandraConfiguration configuration;

    @Inject
    public CassandraRDDPairConsumer(CassandraConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Consumes a JavaRDD applying the consumer function on the list of items of the RDD.
     * This method already take care of open and close the connection to the underline Cassandra.
     *
     * @param rdd      JavaPairRDD.
     * @param consumer Consumer function.
     */
    public void apply(JavaPairRDD<K, V> rdd, CassandraPairConsumer<K, V> consumer) {
        if (!rdd.isEmpty()) {

            VoidFunction<Iterator<Tuple2<K, V>>> fn = (items) -> {
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
    void consumePartition(CassandraPairConsumer<K, V> consumer,
                                  Iterator<Tuple2<K, V>> items,
                                  CassandraConnection connection) {
        if (configuration.isBatchSplitEnabled()) {
            LOGGER.debug("Consuming partitions with split mode");
            consumeItemsSplitted(consumer, items, connection);
        } else {
            LOGGER.debug("Consuming partitions without split mode");
            consumer.consume(items, connection);
        }
    }

    @VisibleForTesting
    void consumeItemsSplitted(CassandraPairConsumer<K, V> consumer,
                                      Iterator<Tuple2<K, V>> items,
                                      CassandraConnection connection) {
        int currentBatchSize = 0;
        List<Tuple2<K, V>> itemList = new LinkedList<>();

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
