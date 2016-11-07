package com.elo7.nightfall.di.providers.spark.batch;

import com.elo7.nightfall.di.AbstractNightfallModule;
import com.elo7.nightfall.di.commons.datapoint.DataPoint;
import com.elo7.nightfall.di.executors.DataPointBatchTaskExecutor;
import com.elo7.nightfall.di.executors.TaskExecutor;
import com.elo7.nightfall.di.tasks.BatchTaskProcessor;
import com.google.inject.TypeLiteral;
import com.netflix.governator.lifecycle.ClasspathScanner;
import org.apache.spark.api.java.JavaRDD;

import javax.inject.Inject;
import javax.inject.Named;

public class DataPointBatchTaskProvider extends AbstractNightfallModule<JavaRDD<DataPoint<String>>> {

	private final String provider;
	private static final TypeLiteral<JavaRDD<DataPoint<String>>> type = new TypeLiteral<JavaRDD<DataPoint<String>>>() {
	};

	@Inject
	protected DataPointBatchTaskProvider(ClasspathScanner scanner, @Named("contextProvider") String provider) {
		super(scanner, type);

		this.provider = provider;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void configure() {
		LOGGER.info("Binding tasks for Batch");
		bindTasks(BatchTaskProcessor.class, provider);
		bind(TaskExecutor.class).to(DataPointBatchTaskExecutor.class);
	}
}
