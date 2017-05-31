package com.elo7.nightfall.riposte.task;

import com.elo7.nightfall.di.task.Task;
import com.elo7.nightfall.di.task.TaskProcessor;
import com.google.inject.Inject;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

@Task
class RiposteTask implements TaskProcessor {

	private static final long serialVersionUID = 1L;
	private static final String VIEW_NAME = "dataSet";

	private final Dataset<Row> dataset;
	private final RiposteConfiguration configuration;
	private final DatasetConsumer consumer;
	private final SparkSession session;

	@Inject
	RiposteTask(
			@Riposte Dataset<Row> dataset,
			RiposteConfiguration configuration,
			DatasetConsumer consumer,
			SparkSession session) {
		this.dataset = dataset;
		this.configuration = configuration;
		this.consumer = consumer;
		this.session = session;
	}

	@Override
	public void process() {
		if (configuration.printSchema()) {
			dataset.printSchema();
		}

		dataset.createOrReplaceTempView(VIEW_NAME);
		consumer.consume(session.sql(configuration.sql()));
	}
}
