package com.elo7.nightfall.di.providers;

import com.elo7.nightfall.di.Component;
import com.elo7.nightfall.di.ExecutionMode;
import com.elo7.nightfall.di.ModuleProvider;
import com.elo7.nightfall.di.NightfallConfigurations;
import com.elo7.nightfall.di.task.Task;
import com.elo7.nightfall.di.task.TaskProcessor;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.netflix.governator.guice.lazy.LazySingletonScope;
import com.netflix.governator.lifecycle.ClasspathScanner;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQueryListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Set;

@ModuleProvider
class SparkModule extends AbstractModule {

	private static final Logger LOGGER = LoggerFactory.getLogger(SparkModule.class);
	private final ClasspathScanner scanner;
	private final ExecutionMode mode;
	private final NightfallConfigurations configurations;

	@Inject
	SparkModule(ClasspathScanner scanner, ExecutionMode mode, NightfallConfigurations configurations) {
		this.scanner = scanner;
		this.mode = mode;
		this.configurations = configurations;
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
				.filter(this::isValidTask)
				.forEach(clazz -> bindTask(taskBinder, clazz));

		if (mode == ExecutionMode.STREAM) {
			LOGGER.info("Binding listeners for Streaming");

			Multibinder<StreamingQueryListener> listenerBinder = Multibinder
					.newSetBinder(binder(), StreamingQueryListener.class);

			scanner.getClasses()
					.stream()
					.filter(this::isStreamListener)
					.forEach(clazz -> bindTask(listenerBinder, clazz));
		} else {
			TypeLiteral<Set<StreamingQueryListener>> type = new StreamingQueryListenerSetTypeLiteral();
			bind(type).toInstance(Collections.emptySet());
		}
	}

	private boolean isValidTask(Class<?> candidate) {
		return TaskProcessor.class.isAssignableFrom(candidate) && candidate.isAnnotationPresent(Task.class);
	}

	private boolean isStreamListener(Class<?> candidate) {
		boolean listenerEnabled = configurations
				.getProperty(candidate.getName())
				.map(BooleanUtils::toBoolean)
				.orElse(false);

		return listenerEnabled
				&& StreamingQueryListener.class.isAssignableFrom(candidate)
				&& candidate.isAnnotationPresent(Component.class);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private void bindTask(Multibinder<?> binder, Class<?> task) {
		LOGGER.debug("Binding class {}.", task.getName());

		boolean isMissingSingletonAnnotation = !task.isAnnotationPresent(Singleton.class)
				&& !task.isAnnotationPresent(javax.inject.Singleton.class);

		if (isMissingSingletonAnnotation) {
			LOGGER.warn("Missing Singleton annotation for class {}.", task.getName());
		}

		binder.addBinding().to((Class) task).in(LazySingletonScope.get());
	}

	private static class StreamingQueryListenerSetTypeLiteral extends TypeLiteral<Set<StreamingQueryListener>> {
	}
}
