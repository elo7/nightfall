package com.elo7.nightfall.di.executors.batch;

import java.time.Duration;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

import com.elo7.nightfall.persistence.cassandra.CassandraConfiguration;
import com.elo7.nightfall.persistence.cassandra.UserCredentials;
import com.netflix.governator.annotations.Configuration;

public class BatchCassandraConfiguration implements CassandraConfiguration {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Configuration("batch.cassandra.hosts")
    private String connectionPoint;
    @Configuration("batch.cassandra.port")
    private int port = 9042;
    @Configuration("batch.cassandra.user")
    private String user;
    @Configuration("batch.cassandra.password")
    private String password;
    @NotBlank
    @Configuration("batch.cassandra.keyspace")
    private String keySpace;
    @Configuration("batch.cassandra.datacenter")
    private String dataCenterName;
    @Configuration("batch.cassandra.history.ttl.days")
    private long taskHistoryTTLDays = 7;
    @Configuration("batch.history.enabled")
    private boolean taskHistoryEnabled = false;

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
    public Optional<String> getDataCenterName() {
        if (StringUtils.isNotBlank(dataCenterName)) {
            return Optional.of(dataCenterName);
        }

        return Optional.empty();
    }

    public int getTaskHistoryTTLInSeconds() {
    	return Long.valueOf(Duration.ofDays(taskHistoryTTLDays).getSeconds()).intValue();
	}

    public boolean isTaskHistoryEnabled() {
		return taskHistoryEnabled;
	}

}
