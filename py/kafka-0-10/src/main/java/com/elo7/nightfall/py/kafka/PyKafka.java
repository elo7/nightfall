package com.elo7.nightfall.py.kafka;

import com.elo7.nightfall.di.NightfallConfigurations;
import com.elo7.nightfall.di.executor.TaskExecutor;
import com.elo7.nightfall.di.providers.kafka.Kafka;
import com.elo7.nightfall.di.task.Task;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

@SuppressWarnings("unused")
@Task
@Singleton
class PyKafka implements TaskExecutor {

	private static final long serialVersionUID = 1L;
	private static Dataset<Row> dataset;
	private static PyKafkaSink sink;
	private static NightfallConfigurations configurations;

	@Inject
	PyKafka(@Kafka Dataset<Row> dataset, PyKafkaSink sink, NightfallConfigurations configurations) {
		PyKafka.dataset = dataset;
		PyKafka.sink = sink;
		PyKafka.configurations = configurations;
	}

	@Override
	public void runTasks() {
		// Do Nothing
	}

	public static Dataset<Row> load() {
		return dataset;
	}

	public static PyKafkaSink sink() {
		return sink;
	}

	public static String getProperty(String key) {
		return configurations.getProperty(key).orElse(null);
	}

}
