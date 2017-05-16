package com.elo7.nightfall.di.providers.kafka;

import com.elo7.nightfall.di.ModuleProvider;
import com.elo7.nightfall.di.NightfallConfigurations;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

@ModuleProvider
public class KafkaModule extends AbstractModule {

	private static final TypeLiteral<Dataset<Row>> TYPE = new TypeLiteral<Dataset<Row>>(){};

	@Override
	protected void configure() {
		bind(TYPE)
				.annotatedWith(Kafka.class)
				.toProvider(KafkaProvider.class);
	}
}
