package com.elo7.nightfall.di.providers.kafka.simple;

import com.elo7.nightfall.di.providers.kafka.topics.KafkaTopicRepository;
import com.google.inject.AbstractModule;
import com.netflix.governator.guice.lazy.LazySingletonScope;
import org.apache.spark.streaming.kafka.KafkaCluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class KafkaSimpleModule extends AbstractModule {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSimpleModule.class);

	private final KafkaSimpleConfiguration configuration;

	@Inject
	KafkaSimpleModule(KafkaSimpleConfiguration configuration) {
		this.configuration = configuration;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void configure() {
		LOGGER.info("Binding Repositories for Kafka Topics");

		try {
			Class clazz = Class.forName(configuration.getRepositoryClass());
			bind(KafkaTopicRepository.class)
					.to(clazz)
					.in(LazySingletonScope.get());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Unknown Kafka Topic Repository Implementation: "
					+ configuration.getRepositoryClass(), e);
		}

		bind(KafkaCluster.class)
				.toProvider(KafkaClusterProvider.class)
				.in(LazySingletonScope.get());
	}
}
