package com.elo7.nightfall.di.providers.kafka;

import com.elo7.nightfall.di.ModuleProvider;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

@ModuleProvider
public class KafkaModule extends AbstractModule {

	private static final TypeLiteral<Dataset<Row>> TYPE = new TypeLiteral<Dataset<Row>>() {
	};

	@Override
	protected void configure() {
		bind(TYPE)
				.annotatedWith(Kafka.class)
				.toProvider(KafkaProvider.class);
	}
}
