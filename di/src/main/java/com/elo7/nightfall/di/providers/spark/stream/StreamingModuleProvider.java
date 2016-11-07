package com.elo7.nightfall.di.providers.spark.stream;

import com.elo7.nightfall.di.AbstractNightfallModule;
import com.elo7.nightfall.di.executors.StreamTaskExecutor;
import com.elo7.nightfall.di.executors.TaskExecutor;
import com.elo7.nightfall.di.tasks.StreamTaskProcessor;
import com.google.inject.TypeLiteral;
import com.netflix.governator.lifecycle.ClasspathScanner;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import javax.inject.Inject;
import javax.inject.Named;

public class StreamingModuleProvider extends AbstractNightfallModule<JavaStreamingContext> {

	private final String provider;

	@Inject
	StreamingModuleProvider(
			ClasspathScanner scanner,
			@Named("contextProvider") String provider) {
		super(scanner, TypeLiteral.get(JavaStreamingContext.class));
		this.provider = provider;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void configure() {
		LOGGER.info("Binding tasks for Stream");
		bindTasks(StreamTaskProcessor.class, provider);
		bind(TaskExecutor.class).to(StreamTaskExecutor.class);
	}

}
