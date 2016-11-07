package com.elo7.nightfall.di.executors;

import com.elo7.nightfall.di.commons.datapoint.DataPoint;
import com.elo7.nightfall.di.commons.datapoint.DataPointValidator;
import com.elo7.nightfall.di.executors.batch.CassandraJobRepository;
import com.elo7.nightfall.di.executors.batch.JobHistory;
import com.elo7.nightfall.di.providers.reporter.ApplicationType;
import com.elo7.nightfall.di.providers.reporter.ReporterSender;
import com.elo7.nightfall.di.tasks.BatchTaskProcessor;
import org.apache.spark.api.java.JavaRDD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Properties;
import java.util.Set;

public class DataPointBatchTaskExecutor implements TaskExecutor {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataPointBatchTaskExecutor.class);

	private final JavaRDD<DataPoint<String>> rdd;
	private final Set<BatchTaskProcessor> processors;
	private final Properties configurations;
	private final CassandraJobRepository repository;
	private final ReporterSender reporter;

	@Inject
	DataPointBatchTaskExecutor(JavaRDD<DataPoint<String>> rdd,
							   Set<BatchTaskProcessor> processors,
							   @Named("ApplicationProperties") Properties configurations,
							   CassandraJobRepository repository,
							   ReporterSender reporter) {
		this.rdd = rdd;
		this.processors = processors;
		this.configurations = configurations;
		this.repository = repository;
		this.reporter = reporter;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void runTasks() {
		try {
			LOGGER.info("Starting processing tasks with configuration[{}].", configurations);
			JobHistory jobHistory = JobHistory.start(rdd.context().appName(), configurations);
			rdd.filter(DataPointValidator::isValid).cache();
			processors.forEach(task -> task.process(rdd));
			repository.persistHistory(jobHistory.finish());
			reporter.sendReport(rdd.context(), ApplicationType.BATCH);
		} catch (Exception e) {
			LOGGER.error("Error while processing tasks", e);
			throw e;
		} finally {
			LOGGER.info("Stopping Spark Context.");
			rdd.context().stop();
		}
	}
}
