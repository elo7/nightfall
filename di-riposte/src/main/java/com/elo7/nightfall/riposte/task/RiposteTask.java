package com.elo7.nightfall.riposte.task;

import com.elo7.nightfall.di.task.Task;
import com.elo7.nightfall.di.task.TaskProcessor;
import com.google.inject.Inject;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Optional;

@Task
class RiposteTask implements TaskProcessor {

	private static final long serialVersionUID = 1L;

	private final Dataset<Row> dataset;
	private final RiposteConfiguration configuration;
	private final DatasetConsumer consumer;

	@Inject
	RiposteTask(@Riposte Dataset<Row> dataset, RiposteConfiguration configuration, DatasetConsumer consumer) {
		this.dataset = dataset;
		this.configuration = configuration;
		this.consumer = consumer;
	}

	@Override
	public void process() {
		Dataset<Row> filtered = dataset
				.select(configuration.query())
				.filter(configuration.filter());

		Dataset<Row> grouped = filtered;
		Optional<Column> groupBy = configuration.groupBy();

		if (groupBy.isPresent()) {
			grouped = filtered
					.groupBy(groupBy.get())
					.count();
		}

		consumer.consume(grouped);
	}
}
