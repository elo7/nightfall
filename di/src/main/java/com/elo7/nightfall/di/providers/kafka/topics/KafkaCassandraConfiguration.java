package com.elo7.nightfall.di.providers.kafka.topics;

import java.time.Duration;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

import com.elo7.nightfall.persistence.cassandra.CassandraConfiguration;
import com.elo7.nightfall.persistence.cassandra.UserCredentials;
import com.netflix.governator.annotations.Configuration;

public class KafkaCassandraConfiguration implements CassandraConfiguration {

    private static final long serialVersionUID = 2L;

    @Configuration("kafka.cassandra.offsetRange.history.ttl.days")
    private long offsetRangeHistoryTTLDays = 7;
    @Configuration("kafka.cassandra.offsetRange.fetch.size")
    private int fetchSize = 100;
    @NotBlank
    @Configuration("kafka.cassandra.hosts")
    private String connectionPoint;
    @Configuration("kafka.cassandra.port")
    private int port = 9042;
    @Configuration("kafka.cassandra.user")
    private String user;
    @Configuration("kafka.cassandra.password")
    private String password;
    @NotBlank
    @Configuration("kafka.cassandra.keyspace")
    private String keySpace;
    @Configuration("kafka.cassandra.datacenter")
    private String dataCenterName;
    @Configuration("task.cassandra.history.enabled")
    private boolean taskHistoryEnabled = false;
    @Configuration("task.cassandra.history.ttl.days")
    private long taskHistoryTTLDays = 7;

    public int getOffsetRangeHistoryTTLInSeconds() {
        return Long.valueOf(Duration.ofDays(offsetRangeHistoryTTLDays).getSeconds()).intValue();
    }

    public int getFetchSize() {
        return fetchSize;
    }

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
