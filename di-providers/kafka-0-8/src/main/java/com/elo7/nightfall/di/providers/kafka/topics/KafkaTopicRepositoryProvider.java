package com.elo7.nightfall.di.providers.kafka.topics;

import com.google.inject.Provider;
import com.google.inject.Provides;
import com.netflix.governator.annotations.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.Set;

public class KafkaTopicRepositoryProvider implements Provider<KafkaTopicRepository> {

	@Configuration("kafka.simple.repository.class")
	private String repositoryClass = CassandraKafkaTopicRepository.class.getName();

	private final Set<KafkaTopicRepository> repositories;

	@Inject
	public KafkaTopicRepositoryProvider(Set<KafkaTopicRepository> repositories) {
		this.repositories = repositories;
	}

	@Override
	@Provides
	@Singleton
	public KafkaTopicRepository get() {

		Optional<KafkaTopicRepository> foundRepository = repositories
				.stream()
				.filter(candidate -> candidate.getClass().getName().equals(repositoryClass))
				.findFirst();

		if (foundRepository.isPresent()) {
			return foundRepository.get();
		}

		throw new RuntimeException("Unknown Kafka Topic Repository Implementation: " + repositoryClass);
	}
}
