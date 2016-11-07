package com.elo7.nightfall.distcp.provider;

import com.elo7.nightfall.di.AbstractNightfallModule;
import com.elo7.nightfall.di.tasks.TaskProcessor;
import com.google.inject.TypeLiteral;
import com.netflix.governator.lifecycle.ClasspathScanner;
import org.apache.spark.api.java.JavaRDD;

import javax.inject.Inject;
import javax.inject.Named;

class DateWindowModule extends AbstractNightfallModule<JavaRDD<String>> {

	private static final TypeLiteral<JavaRDD<String>> type = new TypeLiteral<JavaRDD<String>>() {
	};

	private final String provider;

	@Inject
	DateWindowModule(ClasspathScanner scanner, @Named("contextProvider") String provider) {
		super(scanner, type);

		this.provider = provider;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void configure() {
		LOGGER.info("Binding tasks for Batch");
		bindTasks(TaskProcessor.class, provider);
	}
}
