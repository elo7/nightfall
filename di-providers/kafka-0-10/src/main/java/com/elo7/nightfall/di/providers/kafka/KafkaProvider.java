package com.elo7.nightfall.di.providers.kafka;

import com.elo7.nightfall.di.NightfallConfigurations;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

class KafkaProvider implements Provider<Dataset<Row>> {

	private static final String KAFKA_CONFIG_PREFIX = "spark.kafka.";
	private final NightfallConfigurations configurations;
	private final SparkSession session;

	@Inject
	KafkaProvider(NightfallConfigurations configurations, SparkSession session) {
		this.configurations = configurations;
		this.session = session;
	}

	@Override
	public Dataset<Row> get() {
		return KafkaFactory.load(session, configurations, KAFKA_CONFIG_PREFIX);
	}
}
