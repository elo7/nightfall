package com.elo7.nightfall.persistence.cassandra;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Function to consume items for JavaRDD. Opening and closing connection is already handled.
 *
 * @param <T> Type of the items Iterator.
 */
public interface CassandraConsumer<T> extends Serializable {

    /**
     * Persist the given list of items into Cassandra. Opening and closing connection is already handled.
     *
     * @param items      List of items to be persisted.
     * @param connection Cassandra Connection.
     */
    void consume(Iterator<T> items, CassandraConnection connection);
}
