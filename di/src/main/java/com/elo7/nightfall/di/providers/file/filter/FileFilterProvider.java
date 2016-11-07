package com.elo7.nightfall.di.providers.file.filter;

import com.elo7.nightfall.di.providers.file.FileConfiguration;
import com.elo7.nightfall.di.providers.file.FileFilter;
import com.google.inject.Provider;
import com.google.inject.Provides;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.Set;

class FileFilterProvider implements Provider<FileFilter> {

	private final Set<FileFilter> fileFilters;
	private final FileConfiguration configuration;

	@Inject
	public FileFilterProvider(Set<FileFilter> fileFilters, FileConfiguration configuration) {
		this.fileFilters = fileFilters;
		this.configuration = configuration;
	}

	@Override
	@Provides
	@Singleton
	public FileFilter get() {

		Optional<FileFilter> filter = fileFilters
				.stream()
				.filter(candidate -> candidate.getClass().getName().equals(configuration.getFilterClass()))
				.findFirst();

		if (filter.isPresent()) {
			return filter.get();
		}

		throw new RuntimeException("Unknown File Filter Implementation: " + configuration.getFilterClass());
	}
}
