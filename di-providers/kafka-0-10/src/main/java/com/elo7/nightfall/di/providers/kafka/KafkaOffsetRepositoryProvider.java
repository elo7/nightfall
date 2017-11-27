package com.elo7.nightfall.di.providers.kafka;

import com.elo7.nightfall.di.NightfallConfigurations;
import com.elo7.nightfall.di.providers.kafka.cassandra.CassandraConfiguration;
import com.google.inject.Inject;
import com.google.inject.Provider;

import java.util.Map;

public class KafkaOffsetRepositoryProvider implements Provider<KafkaOffsetRepository> {

	private final Map<String, String> configurations;

	@Inject
	KafkaOffsetRepositoryProvider(@Kafka Map<String, String> configurations) {
		this.configurations = configurations;
	}

	@Override
	public KafkaOffsetRepository get() {
		return new KafkaOffsetRepository(CassandraConfiguration.build(configurations));
	}
}
