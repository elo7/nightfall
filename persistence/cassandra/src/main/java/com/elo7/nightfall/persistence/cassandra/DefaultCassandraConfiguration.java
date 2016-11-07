package com.elo7.nightfall.persistence.cassandra;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.netflix.governator.annotations.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Optional;

public class DefaultCassandraConfiguration implements CassandraConfiguration {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Configuration("cassandra.hosts")
    private String connectionPoint;

    @Configuration("cassandra.port")
    private int port = 9042;

    @Configuration("cassandra.user")
    private String user;

    @Configuration("cassandra.password")
    private String password;

    @NotBlank
    @Configuration("cassandra.keyspace")
    private String keySpace;

    @Configuration("cassandra.datacenter")
    private String dataCenterName;

    @Configuration("cassandra.batch.size")
    private int batchSize = 10000;

    @Configuration("cassandra.batch.split.enabled")
    private boolean batchSplitEnabled = false;

    @Configuration("cassandra.consumer.async.enabled")
    private boolean asyncConsumerEnabled = false;

    @Override
    public String[] getConnectionPoints() {
        return connectionPoint.split(",");
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public Optional<UserCredentials> getUserCredentials() {
        return UserCredentials.createOptional(user, password);
    }

    @Override
    public String getKeySpace() {
        return keySpace;
    }

    @Override
    public boolean isBatchSplitEnabled() {
        return batchSplitEnabled;
    }

    @Override
    public int getBatchSize() {
        return batchSize;
    }

    public boolean isAsyncConsumerEnabled() {
        return asyncConsumerEnabled;
    }

    @Override
    public Optional<String> getDataCenterName() {
        if (StringUtils.isNotBlank(dataCenterName)) {
            return Optional.of(dataCenterName);
        }

        return Optional.empty();
    }

    @VisibleForTesting
    void setDataCenterName(String dataCenterName) {
        this.dataCenterName = dataCenterName;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("connectionPoint", connectionPoint)
                .add("port", port)
                .add("user", user)
                .add("keySpace", keySpace)
                .add("dataCenterName", dataCenterName)
                .add("batchSize", batchSize)
                .add("batchSplitEnabled", batchSplitEnabled)
                .add("asyncConsumerEnabled", asyncConsumerEnabled)
                .toString();
    }
}
