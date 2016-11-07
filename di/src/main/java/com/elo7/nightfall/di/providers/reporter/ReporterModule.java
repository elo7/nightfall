package com.elo7.nightfall.di.providers.reporter;

import com.elo7.nightfall.di.NightfallConfigurations;
import com.elo7.nightfall.di.providers.reporter.console.ConsoleReporterFactory;
import com.google.inject.AbstractModule;
import com.netflix.governator.guice.lazy.LazySingletonScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class ReporterModule extends AbstractModule {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReporterModule.class);
	private final NightfallConfigurations configuration;

	@Inject
	ReporterModule(NightfallConfigurations configuration) {
		this.configuration = configuration;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void configure() {
		LOGGER.info("Binding Implementations for reporters");
		String reporterClass = configuration
				.getProperty("reporter.class")
				.orElse(ConsoleReporterFactory.class.getName());

		try {
			Class clazz = ReporterFactory.class;
			bind(clazz)
					.to(Class.forName(reporterClass))
					.in(LazySingletonScope.get());
		} catch (ClassNotFoundException e) {
			throw new UnknownReporterFactoryException("Unknown ReporterFactory Implementation: " + reporterClass, e);
		}
	}
}
