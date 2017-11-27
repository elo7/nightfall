package com.elo7.nightfall.di.providers.kafka;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.spark.sql.SparkSession;

class KafkaOffsetPersistentListenerProvider implements Provider<KafkaOffsetPersistentListener> {

	private final KafkaOffsetRepository repository;
	private final SparkSession session;
	private final OffsetRangeConverter offsetRangeConverter;

	@Inject
	KafkaOffsetPersistentListenerProvider(KafkaOffsetRepository repository, SparkSession session, OffsetRangeConverter offsetRangeConverter) {
		this.repository = repository;
		this.session = session;
		this.offsetRangeConverter = offsetRangeConverter;
	}

	@Override
	public KafkaOffsetPersistentListener get() {
		return new KafkaOffsetPersistentListener(repository, session.sparkContext().appName(), offsetRangeConverter);
	}
}
