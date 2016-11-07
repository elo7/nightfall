package com.elo7.nightfall.persistence.relational;

import org.apache.spark.api.java.JavaPairRDD;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.Serializable;

/**
 * Helper class to handle RDD to be persisted into a data base.
 *
 * @param <K> Key type of the JavaPairRDD
 * @param <V> Value type of the JavaPairRDD
 */
public class RelationalDataBaseRDDPairConsumer<K, V> implements Serializable {

    private static final long serialVersionUID = 3L;
    private static final Logger LOGGER = LoggerFactory.getLogger(RelationalDataBaseRDDPairConsumer.class);

    private final DataBaseConfiguration configuration;

    @Inject
    public RelationalDataBaseRDDPairConsumer(DataBaseConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Consumes a JavaRDD applying the consumer function on the list of items of the RDD.
     * This method already take care of open and close the connection to the underline data base.
     *
     * @param rdd      JavaPairRDD.
     * @param consumer Consumer function.
     */
    public void apply(JavaPairRDD<K, V> rdd, RDBPairConsumer<K, V> consumer) {
        if (!rdd.isEmpty()) {
            rdd.foreachPartition(items -> {
                if (!items.hasNext()) {
                    LOGGER.debug("Empty iterator, skipping!");
                    return;
                }

                LOGGER.info("Creating connection with DB: {}.", configuration);
                DBI dbi = DBIFactory.create(configuration);

                try (Handle handle = dbi.open()) {
                    LOGGER.debug("Consuming partitions");
                    consumer.consume(items, handle);
                }
            });
        }
    }
}
