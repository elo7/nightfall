package com.elo7.nightfall.di.providers.spark.stream;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.netflix.governator.guice.lazy.LazySingletonScope;
import com.netflix.governator.lifecycle.ClasspathScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class SparkStreamConverterModule extends AbstractModule {

	private static final Logger LOGGER = LoggerFactory.getLogger(SparkStreamConverterModule.class);

	private final ClasspathScanner scanner;

	@Inject
	SparkStreamConverterModule(ClasspathScanner scanner) {
		this.scanner = scanner;
	}

	@Override
	protected void configure() {
		bind(SparkStreamConverter.class)
				.toProvider(SparkStreamConverterFactoryProvider.class)
				.in(LazySingletonScope.get());

		LOGGER.info("Binding Implementations for converters");
		Multibinder<SparkStreamConverter> binder = Multibinder.newSetBinder(binder(), SparkStreamConverter.class);

		scanner
				.getClasses()
				.stream()
				.filter(this::filer)
				.forEach(clazz -> bindImplementation(clazz, binder));
	}

	@SuppressWarnings("unchecked")
	private void bindImplementation(Class<?> clazz, Multibinder<SparkStreamConverter> binder) {
		binder.addBinding().to((Class) clazz).in(LazySingletonScope.get());
	}

	private boolean filer(Class<?> clazz) {
		return SparkStreamConverter.class.isAssignableFrom(clazz);
	}
}
