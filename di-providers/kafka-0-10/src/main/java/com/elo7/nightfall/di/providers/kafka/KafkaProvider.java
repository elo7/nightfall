package com.elo7.nightfall.di.providers.kafka;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Map;

class KafkaProvider implements Provider<Dataset<Row>> {

	private final Map<String, String> configurations;
	private final SparkSession session;
	private final Provider<KafkaOffsetPersistentListener> offsetListenerProvider;
	private final Provider<KafkaOffsetRepository> repositoryProvider;

	@Inject
	KafkaProvider(
			@Kafka Map<String, String> configurations,
			SparkSession session,
			Provider<KafkaOffsetPersistentListener> offsetListenerProvider,
			Provider<KafkaOffsetRepository> repositoryProvider) {
		this.configurations = configurations;
		this.session = session;
		this.offsetListenerProvider = offsetListenerProvider;
		this.repositoryProvider = repositoryProvider;
	}

	@Override
	public Dataset<Row> get() {
		boolean persistentOffsets = BooleanUtils.toBoolean(configurations.get("persistent.offsets"));
		KafkaDatasetBuilder factory = new KafkaDatasetBuilder(session, configurations);

		if (persistentOffsets) {
			factory.withPersistentOffsets(repositoryProvider.get(), offsetListenerProvider.get());
		}

		return factory.build();
	}
}
