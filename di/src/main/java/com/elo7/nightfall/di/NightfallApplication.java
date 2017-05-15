package com.elo7.nightfall.di;

import com.elo7.nightfall.di.task.Task;
import com.elo7.nightfall.di.task.TaskExecutor;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.netflix.governator.guice.LifecycleInjector;
import com.netflix.governator.lifecycle.ClasspathScanner;
import com.netflix.governator.lifecycle.LifecycleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NightfallApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(NightfallApplication.class);

	public static void run(Class<?> source, String[] args) {

		if (source == null) {
			throw new IllegalArgumentException("source cannot be null");
		}

		runExecutor(getNightfallApplication(source), source.getPackage(), args);
	}

	private static Nightfall getNightfallApplication(Class<?> source) {
		if (source.isAnnotationPresent(Nightfall.class)) {
			return source.getAnnotation(Nightfall.class);
		}

		throw new RuntimeException("Missing Nightfall annotation on " + source.getName());
	}

	private static void runExecutor(Nightfall source, Package sourcePackage, String[] args) {
		Injector injector = createInjector(args, sourcePackage);

		try (LifecycleManager manager = injector.getInstance(LifecycleManager.class)) {
			manager.start();
			LOGGER.info("LifecycleManager started");
			injector.getInstance(TaskExecutor.class).runTasks();
		} catch (Exception e) {
			throw new RuntimeException("Failed to start LifecycleManager", e);
		}
	}

	private static Injector createInjector(String[] args, Package sourcePackage) {
		ClasspathScanner scanner = LifecycleInjector.createStandardClasspathScanner(
				Arrays.asList(
						NightfallApplication.class.getPackage().getName(), sourcePackage.getName()),
				Arrays.asList(ModuleProvider.class, Task.class));

		Injector injector = LifecycleInjector
				.builder()
				.usingClasspathScanner(scanner)
				.withModuleClasses(findModules(scanner))
				.withBootstrapModule(new NightfallBootStrapModule(args))
				.inStage(Stage.DEVELOPMENT)
				.build()
				.createInjector();

		LOGGER.info("LifecycleInjector started");
		return injector;
	}

	@SuppressWarnings("unchecked")
	private static List<Class<? extends Module>> findModules(ClasspathScanner scanner) {
		LOGGER.debug("Auto-binding modules annotated with ModuleProvider");
		List<?> modules = scanner.getClasses().stream()
				.filter(NightfallApplication::moduleCandidate)
				.collect(Collectors.toList());

		return (List<Class<? extends Module>>) modules;
	}

	private static boolean moduleCandidate(Class<?> clazz) {
		return AbstractModule.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(ModuleProvider.class);
	}

}