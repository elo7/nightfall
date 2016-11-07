package com.elo7.nightfall.persistence.cassandra;

import com.google.inject.ImplementedBy;

import java.io.Serializable;
import java.util.Optional;

@ImplementedBy(DefaultCassandraConfiguration.class)
public interface CassandraConfiguration extends Serializable {

    int DEFAULT_BATCH_SIZE = 65000;

    String[] getConnectionPoints();

    int getPort();

    Optional<UserCredentials> getUserCredentials();

    String getKeySpace();

    Optional<String> getDataCenterName();

    default boolean isBatchSplitEnabled() {
        return false;
    }

    default int getBatchSize() {
        return DEFAULT_BATCH_SIZE;
    }

    default boolean isAsyncConsumerEnabled() {
        return false;
    }

}
