package com.elo7.nightfall.examples.stream.kafka;

import com.elo7.nightfall.di.task.Task;
import com.elo7.nightfall.di.task.TaskProcessor;
import com.elo7.nightfall.di.providers.kafka.Kafka;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;

@Task
@Singleton
class KafkaExampleTask implements TaskProcessor {

	private static final long serialVersionUID = 1L;
	private final Dataset<Row> events;

	@Inject
	KafkaExampleTask(@Kafka Dataset<Row> events) {
		this.events = events;
	}

	@Override
	public void process() {
		events
				.selectExpr("CAST(value AS STRING)")
				.as(Encoders.STRING())
				.writeStream()
				.format("console")
				.start();
	}
}
