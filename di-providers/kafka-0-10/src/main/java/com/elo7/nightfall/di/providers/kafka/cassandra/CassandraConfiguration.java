package com.elo7.nightfall.di.providers.kafka.cassandra;

import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

public class CassandraConfiguration {

	private final String[] connectionPoint;
	private final String keySpace;
	private final int port;
	private final String user;
	private final String password;
	private final String dataCenterName;
	private final long offsetRangeHistoryTTLDays;

	CassandraConfiguration(String[] connectionPoint, String keySpace, int port, String user, String password,
						   String dataCenterName, int offsetRangeHistoryTTLDays) {
		this.connectionPoint = connectionPoint;
		this.keySpace = keySpace;
		this.port = port;
		this.user = user;
		this.password = password;
		this.dataCenterName = dataCenterName;
		this.offsetRangeHistoryTTLDays = offsetRangeHistoryTTLDays;
	}

	public int getOffsetRangeHistoryTTLInSeconds() {
		return Long.valueOf(Duration.ofDays(offsetRangeHistoryTTLDays).getSeconds()).intValue();
	}

	public String[] getConnectionPoints() {
		return connectionPoint;
	}

	public int getPort() {
		return port;
	}

	public Optional<UserCredentials> getUserCredentials() {
		return UserCredentials.createOptional(user, password);
	}

	public String getKeySpace() {
		return keySpace;
	}

	public Optional<String> getDataCenterName() {
		if (StringUtils.isNotBlank(dataCenterName)) {
			return Optional.of(dataCenterName);
		}

		return Optional.empty();
	}

	public static CassandraConfiguration build(Map<String, String> configuration) {
		String connectionPoint = configuration.get("cassandra.hosts");
		String keyspace = configuration.get("cassandra.keyspace");

		if (StringUtils.isBlank(connectionPoint) || StringUtils.isBlank(keyspace)) {
			throw new IllegalArgumentException("cassandra.hosts and/or cassandra.keyspace cannot be blank");
		}

		String port = configuration.getOrDefault("cassandra.port", "9042");
		String offsetRangeHistoryTTLDays = configuration.getOrDefault("offsetRange.history.ttl.days", "7");

		return new CassandraConfiguration(
				connectionPoint.split(","),
				keyspace,
				Integer.parseInt(port),
				configuration.get("cassandra.user"),
				configuration.get("cassandra.password"),
				configuration.get("cassandra.datacenter"),
				Integer.parseInt(offsetRangeHistoryTTLDays)

		);
	}
}
