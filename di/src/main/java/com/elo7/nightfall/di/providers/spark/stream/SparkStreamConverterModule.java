package com.elo7.nightfall.di.providers.spark.stream;

import com.elo7.nightfall.di.NightfallConfigurations;
import com.google.inject.AbstractModule;
import com.netflix.governator.guice.lazy.LazySingletonScope;

import javax.inject.Inject;

public class SparkStreamConverterModule extends AbstractModule {

	private final NightfallConfigurations configuration;

	@Inject
	SparkStreamConverterModule(NightfallConfigurations configuration) {
		this.configuration = configuration;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void configure() {
		String converterClass = configuration
				.getProperty("stream.provider.converter")
				.orElse(DataPointStreamContextConverter.class.getName());

		try {
			Class converter = SparkStreamConverter.class;
			bind(converter)
					.to(Class.forName(converterClass))
					.in(LazySingletonScope.get());
		} catch (ClassNotFoundException e) {
			throw new UnknownSparkConverterException("Unknown SparkStreamConverter Implementation: "
					+ converterClass, e);
		}
	}
}
