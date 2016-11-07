package com.elo7.nightfall.distcp.provider;

import com.elo7.nightfall.di.tasks.TaskProcessor;
import org.apache.spark.api.java.JavaRDD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Set;

class DateWindowExecutor implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(DateWindowExecutor.class);

	private final JavaRDD<String> rdd;
	private final Set<TaskProcessor> processors;

	@Inject
	DateWindowExecutor(JavaRDD<String> rdd, Set<TaskProcessor> processors) {
		this.rdd = rdd;
		this.processors = processors;
	}

	@Override
	public void run() {
		try {
			processors.forEach(task -> task.process(rdd));
		} catch (Exception e) {
			LOGGER.error("Error while processing tasks", e);
			throw e;
		} finally {
			LOGGER.info("Stopping Spark Context.");
			rdd.context().stop();
		}
	}
}
