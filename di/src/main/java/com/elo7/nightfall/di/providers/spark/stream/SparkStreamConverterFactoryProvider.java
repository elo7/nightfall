package com.elo7.nightfall.di.providers.spark.stream;

import com.google.inject.Provider;
import com.google.inject.Provides;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.Set;

public class SparkStreamConverterFactoryProvider implements Provider<SparkStreamConverter> {

	private final Set<SparkStreamConverter> converters;
	private final StreamingConfiguration streamingConfiguration;

	@Inject
	public SparkStreamConverterFactoryProvider(Set<SparkStreamConverter> converters, StreamingConfiguration streamingConfiguration) {
		this.converters = converters;
		this.streamingConfiguration = streamingConfiguration;
	}

	@Override
	@Provides
	@Singleton
	public SparkStreamConverter get() {
		Optional<SparkStreamConverter> foundRepository = converters
				.stream()
				.filter(candidate -> candidate.getClass().getName().equals(streamingConfiguration.getStreamConverter()))
				.findFirst();

		if (foundRepository.isPresent()) {
			return foundRepository.get();
		}

		throw new UnknownSparkConverterException("Unknown SparkStreamConverter Implementation: " + streamingConfiguration.getStreamConverter());
	}
}
