package com.elo7.nightfall.di.providers.kafka.simple;

import com.elo7.nightfall.di.providers.kafka.topics.KafkaTopicRepository;
import com.elo7.nightfall.di.providers.kafka.topics.KafkaTopicRepositoryProvider;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.netflix.governator.guice.lazy.LazySingletonScope;
import com.netflix.governator.lifecycle.ClasspathScanner;
import org.apache.spark.streaming.kafka.KafkaCluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class KafkaSimpleModule extends AbstractModule {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSimpleModule.class);

	private final ClasspathScanner scanner;

	@Inject
	KafkaSimpleModule(ClasspathScanner scanner) {
		this.scanner = scanner;
	}

	@Override
	protected void configure() {

		bind(KafkaTopicRepository.class)
				.toProvider(KafkaTopicRepositoryProvider.class)
				.in(LazySingletonScope.get());

		bind(KafkaCluster.class)
				.toProvider(KafkaClusterProvider.class)
				.in(LazySingletonScope.get());

		LOGGER.info("Binding Repositories for Kafka Topics");
		Multibinder<KafkaTopicRepository> binder = Multibinder.newSetBinder(binder(), KafkaTopicRepository.class);

		scanner
				.getClasses()
				.stream()
				.filter(this::filer)
				.forEach(clazz -> bindRepo(clazz, binder));
	}

	@SuppressWarnings("unchecked")
	private void bindRepo(Class<?> clazz, Multibinder<KafkaTopicRepository> binder) {
		binder.addBinding().to((Class) clazz).in(LazySingletonScope.get());
	}

	private boolean filer(Class<?> clazz) {
		return KafkaTopicRepository.class.isAssignableFrom(clazz);
	}
}
