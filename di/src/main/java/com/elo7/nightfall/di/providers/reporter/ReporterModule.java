package com.elo7.nightfall.di.providers.reporter;

import com.elo7.nightfall.di.ModuleProvider;
import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@ModuleProvider
class ReporterModule extends AbstractModule{

	private static final Logger LOGGER = LoggerFactory.getLogger(ReporterModule.class);
	private final ReporterConfiguration configuration;

	@Inject
	ReporterModule(final ReporterConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	protected void configure() {
		LOGGER.info("Binding Implementations for reporters");
		if(configuration.isReporterEnabled()){
//			String reporterClass = configuration
//					.getClass()
//					.orElse(ConsoleReporterFactory.class.getName());
//
//			try {
//				Class clazz = ReporterFactory.class;
//				bind(clazz)
//						.to(Class.forName(reporterClass))
//						.in(LazySingletonScope.get());
//			} catch (ClassNotFoundException e) {
//				throw new UnknownReporterFactoryException("Unknown ReporterFactory Implementation: " + reporterClass, e);
//			}
		}
	}
}
