package com.elo7.nightfall.di;

import com.elo7.nightfall.di.executor.TaskExecutor;
import com.elo7.nightfall.di.task.Task;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.netflix.governator.guice.LifecycleInjector;
import com.netflix.governator.lifecycle.ClasspathScanner;
import com.netflix.governator.lifecycle.LifecycleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		Injector injector = createInjector(args, sourcePackage, source);

		try (LifecycleManager manager = injector.getInstance(LifecycleManager.class)) {
			manager.start();
			LOGGER.info("LifecycleManager started");
			injector.getInstance(TaskExecutor.class).runTasks();
		} catch (Exception e) {
			throw new RuntimeException("Failed to start LifecycleManager", e);
		}
	}

	private static Injector createInjector(String[] args, Package sourcePackage, Nightfall nightfall) {
		ClasspathScanner scanner = LifecycleInjector.createStandardClasspathScanner(
				scanPackages(sourcePackage, nightfall),
				Arrays.asList(ModuleProvider.class, Task.class));

		Injector injector = LifecycleInjector
				.builder()
				.usingClasspathScanner(scanner)
				.withModuleClasses(findModules(scanner))
				.withBootstrapModule(new NightfallBootStrapModule(args, nightfall.value()))
				.inStage(Stage.DEVELOPMENT)
				.build()
				.createInjector();

		LOGGER.info("LifecycleInjector started");
		return injector;
	}

	private static List<String> scanPackages(Package sourcePackage, Nightfall nightfall) {
		List<String> packages = new ArrayList<>();

		packages.add(NightfallApplication.class.getPackage().getName());

		if (nightfall.scanPackages().length > 0) {
			Stream.of(nightfall.scanPackages())
					.forEach(packages::add);
		} else {
			packages.add(sourcePackage.getName());
		}

		return packages;
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