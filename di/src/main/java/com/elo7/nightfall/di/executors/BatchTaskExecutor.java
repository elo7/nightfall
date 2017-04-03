package com.elo7.nightfall.di.executors;

import com.elo7.nightfall.di.NightfallConfigurations;
import com.elo7.nightfall.di.executors.batch.JobHistory;
import com.elo7.nightfall.di.executors.batch.JobHistoryRepository;
import com.elo7.nightfall.di.providers.reporter.ApplicationType;
import com.elo7.nightfall.di.providers.reporter.ReporterSender;
import com.elo7.nightfall.di.tasks.BatchTaskProcessor;
import org.apache.spark.api.java.JavaRDD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Set;

public class BatchTaskExecutor implements TaskExecutor {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataPointBatchTaskExecutor.class);

	private final JavaRDD<String> rdd;
	private final Set<BatchTaskProcessor> processors;
	private final NightfallConfigurations configurations;
	private final JobHistoryRepository repository;
	private final ReporterSender reporter;

	@Inject
	BatchTaskExecutor(JavaRDD<String> rdd,
					  Set<BatchTaskProcessor> processors,
					  NightfallConfigurations configurations,
					  JobHistoryRepository repository,
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
