package com.elo7.nightfall.examples.batch.simple;

import com.elo7.nightfall.di.task.TaskProcessor;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;

import javax.inject.Inject;
import java.util.Arrays;

public class SimpleBatchTask implements TaskProcessor {

	private static final long serialVersionUID = 1L;

	private final SparkSession session;

	@Inject
	SimpleBatchTask(final SparkSession session) {
		this.session = session;
	}

	@Override
	public void process() {
		session.createDataset(Arrays.asList("input 1", "input 2", "invalid input"), Encoders.STRING())
				.show();
	}
}
