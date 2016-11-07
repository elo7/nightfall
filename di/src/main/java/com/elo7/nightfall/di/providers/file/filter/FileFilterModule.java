package com.elo7.nightfall.di.providers.file.filter;

import com.elo7.nightfall.di.providers.file.FileConfiguration;
import com.elo7.nightfall.di.providers.file.FileFilter;
import com.google.inject.AbstractModule;
import com.netflix.governator.guice.lazy.LazySingletonScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class FileFilterModule extends AbstractModule {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileFilterModule.class);

	private final FileConfiguration configuration;

	@Inject
	FileFilterModule(FileConfiguration configuration) {
		this.configuration = configuration;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void configure() {
		LOGGER.info("Binding FileFilters for FileRDD");

		try {
			Class clazz = Class.forName(configuration.getFilterClass());
			bind(FileFilter.class)
					.to(clazz)
					.in(LazySingletonScope.get());

		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Unknown File Filter Implementation: " + configuration.getFilterClass(), e);
		}
	}
}
