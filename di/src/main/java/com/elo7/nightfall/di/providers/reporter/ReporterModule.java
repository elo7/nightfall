package com.elo7.nightfall.di.providers.reporter;

import com.google.inject.AbstractModule;
import com.netflix.governator.guice.lazy.LazySingletonScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class ReporterModule extends AbstractModule {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReporterModule.class);
	private final ReporterConfiguration configuration;

	@Inject
	ReporterModule(ReporterConfiguration configuration) {
		this.configuration = configuration;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void configure() {
		try {
			LOGGER.info("Binding Implementations for reporters");
			Class clazz = ReporterFactory.class;
			bind(clazz)
					.to(Class.forName(configuration.getReporterClass()))
					.in(LazySingletonScope.get());
		} catch (ClassNotFoundException e) {
			throw new UnknownReporterFactoryException("Unknown ReporterFactory Implementation: "
					+ configuration.getReporterClass(), e);
		}
	}
}
