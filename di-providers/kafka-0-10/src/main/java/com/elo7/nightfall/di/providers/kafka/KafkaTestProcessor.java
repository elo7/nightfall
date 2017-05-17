package com.elo7.nightfall.di.providers.kafka;

import com.elo7.nightfall.di.task.Task;
import com.elo7.nightfall.di.task.TaskProcessor;
import com.google.inject.Inject;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;

@Task
public class KafkaTestProcessor implements TaskProcessor {

	private final Dataset<Row> events;

	@Inject
	KafkaTestProcessor(@Kafka Dataset<Row> events) {
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
