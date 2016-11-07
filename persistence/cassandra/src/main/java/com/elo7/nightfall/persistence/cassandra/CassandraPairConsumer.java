package com.elo7.nightfall.persistence.cassandra;

import scala.Tuple2;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Function to consume items for JavaPairRDD. Opening and closing connection is already handled.
 *
 * @param <K> Type of Key.
 * @param <V> Type of Value.
 */
public interface CassandraPairConsumer<K, V> extends Serializable {

    /**
     * Persist the given list of items. Opening and closing connection is already handled.
     *
     * @param items      Iterator of items to be persisted.
     * @param connection Cassandra Connection.
     */
    void consume(Iterator<Tuple2<K, V>> items, CassandraConnection connection);
}
