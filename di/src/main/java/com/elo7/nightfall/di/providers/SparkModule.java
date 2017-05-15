package com.elo7.nightfall.di.providers;

import com.elo7.nightfall.di.ModuleProvider;
import com.elo7.nightfall.di.task.TaskProcessor;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.netflix.governator.guice.lazy.LazySingletonScope;
import com.netflix.governator.lifecycle.ClasspathScanner;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@ModuleProvider
class SparkModule extends AbstractModule {

	private static final Logger LOGGER = LoggerFactory.getLogger(SparkModule.class);
	private final ClasspathScanner scanner;

	@Inject
	SparkModule(final ClasspathScanner scanner) {
		this.scanner = scanner;
	}

	@Override
	protected void configure() {
		binder()
				.bind(SparkSession.class)
				.toProvider(SparkSessionProvider.class);

		Multibinder<TaskProcessor> taskBinder = Multibinder.newSetBinder(binder(), TaskProcessor.class);

		LOGGER.info("Binding TaskProcessors");
		scanner.getClasses()
				.stream()
				.filter(TaskProcessor.class::isAssignableFrom)
				.forEach(clazz -> bindTask(taskBinder, clazz));

	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private void bindTask(Multibinder<?> binder, Class<?> task) {
		LOGGER.debug("Binding task {}.", task.getName());

		boolean isMissingSingletonAnnotation = !task.isAnnotationPresent(Singleton.class)
				&& !task.isAnnotationPresent(javax.inject.Singleton.class);

		if (isMissingSingletonAnnotation) {
			LOGGER.warn("Missing Singleton annotation for task {}.", task.getName());
		}

		binder.addBinding().to((Class) task).in(LazySingletonScope.get());
	}
}
