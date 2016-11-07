package com.elo7.nightfall.persistence.relational;

import org.skife.jdbi.v2.Handle;
import scala.Tuple2;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Function to consume items for JavaPairRDD. Opening and closing connection is already handled.
 *
 * @param <K> Type of Key.
 * @param <V> Type of Value.
 */
public interface RDBPairConsumer<K, V> extends Serializable {

    /**
     * Persist the given list of items into a data base. Opening and closing connection is already handled.
     *
     * @param items  List of items to be persisted.
     * @param handle DBI Handle
     */
    void consume(Iterator<Tuple2<K, V>> items, Handle handle);
}
