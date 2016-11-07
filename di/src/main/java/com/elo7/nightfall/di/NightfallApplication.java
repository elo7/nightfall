package com.elo7.nightfall.di;

import com.elo7.nightfall.di.executors.MissingExecutorException;
import com.elo7.nightfall.di.executors.TaskExecutor;
import com.elo7.nightfall.di.executors.batch.JobHistoryModule;
import com.elo7.nightfall.di.providers.ExecutorProvider;
import com.elo7.nightfall.di.providers.reporter.ReporterModule;
import com.elo7.nightfall.di.tasks.Task;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.netflix.governator.guice.LifecycleInjector;
import com.netflix.governator.lifecycle.ClasspathScanner;
import com.netflix.governator.lifecycle.LifecycleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class NightfallApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(NightfallApplication.class);

	public static void run(Class<?> source, String[] args) {

		if (source == null) {
			throw new IllegalArgumentException("source cannot be null");
		}

		ExecutorProvider provider = getExecutorProvider(source);
		runExecutor(provider, source.getPackage(), args);
	}

	private static ExecutorProvider getExecutorProvider(Class<?> source) {
		return Arrays.stream(source.getAnnotations())
				.filter(annotation -> annotation.annotationType().isAnnotationPresent(ExecutorProvider.class))
				.map(annotation -> annotation.annotationType().getAnnotation(ExecutorProvider.class))
				.findFirst().orElseThrow(MissingExecutorException::new);
	}

	private static void runExecutor(ExecutorProvider provider, Package sourcePackage, String[] args) {
		Injector injector = createInjector(args, provider, sourcePackage);

		try (LifecycleManager manager = injector.getInstance(LifecycleManager.class)) {
			manager.start();
			LOGGER.info("LifecycleManager started");
			injector.getInstance(TaskExecutor.class).runTasks();
		} catch (Exception e) {
			throw new RuntimeException("Failed to start LifecycleManager", e);
		}
	}

	@SuppressWarnings("ConfusingArgumentToVarargsMethod")
	private static Injector createInjector(String[] args, ExecutorProvider provider, Package sourcePackage) {

		ClasspathScanner scanner = LifecycleInjector.createStandardClasspathScanner(
				Arrays.asList(NightfallApplication.class.getPackage().getName(), sourcePackage.getName()),
				Arrays.asList(Task.class, Component.class)
		);
		Injector injector = LifecycleInjector
				.builder()
				.usingClasspathScanner(scanner)
				.withModuleClasses(provider.module(), ReporterModule.class, JobHistoryModule.class)
				.withModuleClasses(provider.additionalModules())
				.withBootstrapModule(new NightfallBootStrapModule(args, provider.provider()))
				.inStage(Stage.DEVELOPMENT)
				.build()
				.createInjector();

		LOGGER.info("LifecycleInjector started");

		return injector;
	}
}
