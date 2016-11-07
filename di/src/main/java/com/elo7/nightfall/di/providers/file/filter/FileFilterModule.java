package com.elo7.nightfall.di.providers.file.filter;

import com.elo7.nightfall.di.providers.file.FileFilter;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.netflix.governator.guice.lazy.LazySingletonScope;
import com.netflix.governator.lifecycle.ClasspathScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class FileFilterModule extends AbstractModule {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileFilterModule.class);

	private final ClasspathScanner scanner;

	@Inject
	FileFilterModule(ClasspathScanner scanner) {
		this.scanner = scanner;
	}

	@Override
	protected void configure() {

		bind(FileFilter.class)
				.toProvider(FileFilterProvider.class)
				.in(LazySingletonScope.get());

		LOGGER.info("Binding FileFilters for FileRDD");
		Multibinder<FileFilter> binder = Multibinder.newSetBinder(binder(), FileFilter.class);

		scanner
				.getClasses()
				.stream()
				.filter(this::filer)
				.forEach(clazz -> bindRepo(clazz, binder));
	}

	@SuppressWarnings("unchecked")
	private void bindRepo(Class<?> clazz, Multibinder<FileFilter> binder) {
		binder.addBinding().to((Class) clazz).in(LazySingletonScope.get());
	}

	private boolean filer(Class<?> clazz) {
		return FileFilter.class.isAssignableFrom(clazz);
	}
}
