package com.elo7.nightfall.examples.batch.simple;

import com.elo7.nightfall.di.task.Task;
import com.elo7.nightfall.di.task.TaskProcessor;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;

import java.util.Arrays;

@Task
@Singleton
public class SimpleBatchTask implements TaskProcessor {

	private static final long serialVersionUID = 1L;

	private final SparkSession session;

	@Inject
	SimpleBatchTask(SparkSession session) {
		this.session = session;
	}

	@Override
	public void process() {
		session.createDataset(Arrays.asList("input 1", "input 2", "invalid input"), Encoders.STRING())
				.show();
	}
}
