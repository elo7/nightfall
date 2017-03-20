package com.elo7.nightfall.di.executors.batch;

import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class JobHistoryModule extends AbstractModule {

	private static final Logger LOGGER = LoggerFactory.getLogger(JobHistoryModule.class);

	private final BatchConfiguration configuration;

	@Inject
	JobHistoryModule(BatchConfiguration configuration) {
		this.configuration = configuration;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void configure() {
		try {
			Class clazz = JobHistoryRepository.class;
			bind(clazz).to(Class.forName(configuration.getJobHistoryRepositoryClass()));
		} catch (ClassNotFoundException e) {
			LOGGER.debug("Failed to bind JobHistoryRepository to {}.", configuration.getJobHistoryRepositoryClass(), e);
			throw new RuntimeException("Failed to bind JobHistoryRepository to "
					+ configuration.getJobHistoryRepositoryClass());
		}
	}
}
