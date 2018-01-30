package com.elo7.nightfall.py.kafka;

import com.elo7.nightfall.di.executor.TaskExecutor;
import com.elo7.nightfall.di.providers.kafka.Kafka;
import com.elo7.nightfall.di.task.Task;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

@Task
@Singleton
class PyExecutor implements TaskExecutor {

	private static final long serialVersionUID = 1L;
	private static Dataset<Row> dataset;
	private static PyKafkaSink writer;

	@Inject
	PyExecutor(@Kafka Dataset<Row> dataset, PyKafkaSink writer) {
		PyExecutor.dataset = dataset;
		PyExecutor.writer = writer;
	}

	@Override
	public void runTasks() {
		// Do Nothing
	}

	static Dataset<Row> load() {
		return dataset;
	}

	static void sink(Dataset<?> dataset, String configPrefix) {
		writer.writer(dataset, configPrefix);
	}

}
