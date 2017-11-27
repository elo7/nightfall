package com.elo7.nightfall.di.providers.kafka;

import com.elo7.nightfall.di.ModuleProvider;
import com.elo7.nightfall.di.NightfallConfigurations;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import scala.Tuple2;

import java.util.Map;
import java.util.stream.Collectors;

@ModuleProvider
public class KafkaModule extends AbstractModule {

	private static final String KAFKA_CONFIG_PREFIX = "spark.kafka.";
	private static final TypeLiteral<Dataset<Row>> TYPE = new TypeLiteral<Dataset<Row>>() {
	};
	private static final TypeLiteral<Map<String, String>> CONFIG_TYPE = new TypeLiteral<Map<String, String>>() {
	};

	private final NightfallConfigurations configurations;

	@Inject
	KafkaModule(NightfallConfigurations configurations) {
		this.configurations = configurations;
	}

	@Override
	protected void configure() {
		bind(TYPE)
				.annotatedWith(Kafka.class)
				.toProvider(KafkaProvider.class);
		bind(KafkaOffsetPersistentListener.class).toProvider(KafkaOffsetPersistentListenerProvider.class);
		bind(KafkaOffsetRepository.class).toProvider(KafkaOffsetRepositoryProvider.class);

		Map<String, String> kafkaConfigs = configurations.getPropertiesWithPrefix(KAFKA_CONFIG_PREFIX)
				.entrySet()
				.stream()
				.map(entry -> new Tuple2<>(entry.getKey().replaceFirst(KAFKA_CONFIG_PREFIX, ""), entry.getValue()))
				.collect(Collectors.toMap(Tuple2::_1, Tuple2::_2));

		bind(CONFIG_TYPE)
				.annotatedWith(Kafka.class)
				.toInstance(kafkaConfigs);
	}
}
