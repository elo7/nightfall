package com.elo7.nightfall.di.providers.file.filter;

import com.elo7.nightfall.di.NightfallConfigurations;
import com.elo7.nightfall.di.providers.file.FileFilter;
import com.google.inject.AbstractModule;
import com.netflix.governator.guice.lazy.LazySingletonScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class FileFilterModule extends AbstractModule {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileFilterModule.class);

	private final NightfallConfigurations configuration;

	@Inject
	FileFilterModule(NightfallConfigurations configuration) {
		this.configuration = configuration;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void configure() {
		LOGGER.info("Binding FileFilters for FileRDD");
		String filterClazz = configuration
				.getProperty("file.filter.class")
				.orElse(FakeFileFilter.class.getName());

		try {
			Class clazz = Class.forName(filterClazz);
			bind(FileFilter.class)
					.to(clazz)
					.in(LazySingletonScope.get());

		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Unknown File Filter Implementation: " + filterClazz, e);
		}
	}
}
