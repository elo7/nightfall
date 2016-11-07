package com.elo7.nightfall.persistence.relational;

import org.skife.jdbi.v2.Handle;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Function to consume items for JavaRDD. Opening and closing connection is already handled.
 *
 * @param <T> List of items to be persisted.
 */
public interface RDBConsumer<T> extends Serializable {

    /**
     * Persist the given list of items into a data base. Opening and closing connection is already handled.
     *
     * @param items  List of items to be persisted.
     * @param handle DBI Handle
     */
    void consume(Iterator<T> items, Handle handle);
}
