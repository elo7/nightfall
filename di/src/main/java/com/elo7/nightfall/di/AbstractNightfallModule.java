package com.elo7.nightfall.di;

import com.elo7.nightfall.di.tasks.Task;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.netflix.governator.guice.lazy.LazySingletonScope;
import com.netflix.governator.lifecycle.ClasspathScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractNightfallModule<T> extends AbstractModule {

	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractNightfallModule.class);

	private final ClasspathScanner scanner;
	private final TypeLiteral<T> type;

	protected AbstractNightfallModule(ClasspathScanner scanner, TypeLiteral<T> type) {
		this.scanner = scanner;
		this.type = type;
	}

	@SuppressWarnings("unchecked")
	protected void bindTasks(Class<?> processor, String provider) {
		try {
			bind(type)
					.toProvider((Class<? extends Provider<T>>) Class.forName(provider))
					.in(LazySingletonScope.get());

			Multibinder<?> binder = Multibinder.newSetBinder(binder(), processor);

			scanner
					.getClasses()
					.stream()
					.filter(clazz -> filterTasks(clazz, processor))
					.forEach(clazz -> bindTask(binder, clazz));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean filterTasks(Class<?> clazz, Class<?> processor) {
		return clazz.isAnnotationPresent(Task.class) && processor.isAssignableFrom(clazz);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private void bindTask(Multibinder<?> binder, Class<?> task) {
		LOGGER.info("Binding task {}.", task.getName());

		boolean isMissingSingletonAnnotation = !task.isAnnotationPresent(Singleton.class)
				&& !task.isAnnotationPresent(javax.inject.Singleton.class);

		if (isMissingSingletonAnnotation) {
			LOGGER.info("Missing Singleton annotation for task {}.", task.getName());
		}

		binder.addBinding().to((Class) task).in(LazySingletonScope.get());
	}

	protected ClasspathScanner getScanner() {
		return scanner;
	}
}
