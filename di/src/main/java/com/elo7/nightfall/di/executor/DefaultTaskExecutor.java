package com.elo7.nightfall.di.executor;

import com.elo7.nightfall.di.task.TaskProcessor;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Set;

public class DefaultTaskExecutor implements TaskExecutor {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTaskExecutor.class);
	private final Set<TaskProcessor> tasks;
	private final SparkSession session;

	@Inject
	DefaultTaskExecutor(Set<TaskProcessor> tasks, SparkSession session) {
		this.tasks = tasks;
		this.session = session;
	}

	@Override
	public void runTasks() {
		try {
			tasks.forEach(TaskProcessor::process);

			if (session.streams().active().length > 0) {
				session.streams().awaitAnyTermination();
			}
		} catch (StreamingQueryException e) {
			LOGGER.error("Failed to execute Spark Session", e);
		} finally {
			session.stop();
		}
	}
}
