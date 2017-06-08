package com.elo7.nightfall.di;

import com.elo7.nightfall.di.function.Consumer;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQueryException;

public enum ExecutionMode {

	BATCH(session -> {
	}),
	STREAM(session -> {
		try {
			session.streams().awaitAnyTermination();
		} catch (StreamingQueryException e) {
			throw new RuntimeException(e);
		}
	});

	private final Consumer<SparkSession> consumer;

	ExecutionMode(Consumer<SparkSession> consumer) {
		this.consumer = consumer;
	}

	public void afterTasks(SparkSession session) {
		if (consumer != null) {
			consumer.apply(session);
		}
	}
}
