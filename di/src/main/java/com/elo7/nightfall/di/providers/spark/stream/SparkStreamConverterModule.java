package com.elo7.nightfall.di.providers.spark.stream;

import com.google.inject.AbstractModule;
import com.netflix.governator.guice.lazy.LazySingletonScope;

import javax.inject.Inject;

public class SparkStreamConverterModule extends AbstractModule {

	private final StreamingConfiguration configuration;

	@Inject
	SparkStreamConverterModule(StreamingConfiguration configuration) {
		this.configuration = configuration;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void configure() {
		try {
			Class converter = SparkStreamConverter.class;
			bind(converter)
					.to(Class.forName(configuration.getStreamConverter()))
					.in(LazySingletonScope.get());
		} catch (ClassNotFoundException e) {
			throw new UnknownSparkConverterException("Unknown SparkStreamConverter Implementation: "
					+ configuration.getStreamConverter(), e);
		}
	}
}
