package com.elo7.nightfall.di.providers.reporter;

import com.elo7.nightfall.di.ModuleProvider;
import com.google.inject.AbstractModule;
import com.netflix.governator.guice.lazy.LazySingletonScope;
import org.apache.spark.sql.streaming.StreamingQueryListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@ModuleProvider
class ReporterModule extends AbstractModule {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReporterModule.class);
	private final ReporterConfiguration configuration;

	@Inject
	ReporterModule(final ReporterConfiguration configuration) {
		this.configuration = configuration;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void configure() {
		LOGGER.info("Binding Implementations for reporters");
		if (configuration.isReporterEnabled()) {
			String reporterClass = configuration.getReporterClass();

			try {
				bind(StreamingQueryListener.class)
						.to((Class<? extends StreamingQueryListener>) Class.forName(configuration.getReporterClass()))
						.in(LazySingletonScope.get());
			} catch (ClassNotFoundException e) {
				throw new UnknownReporterFactoryException("Unknown ReporterFactory Implementation: " + reporterClass, e);
			}
		} else {
			bind(StreamingQueryListener.class)
					.to(DisabledReporter.class)
					.in(LazySingletonScope.get());
		}
	}
}
