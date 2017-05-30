package com.elo7.nightfall.riposte.task;

import com.elo7.nightfall.di.task.Task;
import com.elo7.nightfall.di.task.TaskProcessor;
import com.google.inject.Inject;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

@Task
class RiposteTask implements TaskProcessor {

	private static final long serialVersionUID = 1L;

	private final Dataset<Row> dataset;
	private final RiposteConfiguration configuration;
	private final DatasetConsumer consumer;
	private final DatasetSelect select;

	@Inject
	RiposteTask(
			@Riposte Dataset<Row> dataset,
			RiposteConfiguration configuration,
			DatasetConsumer consumer,
			DatasetSelect select) {
		this.dataset = dataset;
		this.configuration = configuration;
		this.consumer = consumer;
		this.select = select;
	}

	@Override
	public void process() {
		if (configuration.printSchema()) {
			dataset.printSchema();
		}

		Dataset<Row> result = select.apply(dataset);

		result = applyFilter(result);
		result = applyGroupBy(result);

		consumer.consume(result);
	}

	private Dataset<Row> applyFilter(Dataset<Row> dataset) {
		return configuration.filter().map(dataset::filter).orElse(dataset);
	}

	private Dataset<Row> applyGroupBy(Dataset<Row> dataset) {
		return configuration.groupBy().map(groupBy -> dataset.groupBy(groupBy).count()).orElse(dataset);
	}
}
