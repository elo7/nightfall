package com.elo7.nightfall.di.task;

import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;

import javax.inject.Inject;
import java.util.Arrays;

@Task
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
