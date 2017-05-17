package com.elo7.nightfall.di.executor;

import com.elo7.nightfall.di.ExecutionMode;
import com.elo7.nightfall.di.task.TaskProcessor;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Set;

public class DefaultTaskExecutor implements TaskExecutor {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTaskExecutor.class);
	private final Set<TaskProcessor> tasks;
	private final SparkSession session;
	private final ExecutionMode mode;

	@Inject
	DefaultTaskExecutor(Set<TaskProcessor> tasks, SparkSession session, ExecutionMode mode) {
		this.tasks = tasks;
		this.session = session;
		this.mode = mode;
	}

	@Override
	public void runTasks() {
		try {
			LOGGER.info("Initializing tasks execution.");
			tasks.forEach(TaskProcessor::process);
			mode.afterTasks(session);
		} finally {
			session.stop();
		}
	}
}
