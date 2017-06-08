package com.elo7.nightfall.di.executor;

import com.elo7.nightfall.di.task.Task;
import com.elo7.nightfall.di.task.TaskProcessor;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;

@Task
@Singleton
public class TestTaskProcessor implements TaskProcessor {

	private final SparkSession session;

	@Inject
	TestTaskProcessor(final SparkSession session) {
		this.session = session;
	}

	@Override
	public void process() {
		session.createDataset(Arrays.asList("input 1", "input 2", "invalid input"), Encoders.STRING())
				.show();
	}
}
