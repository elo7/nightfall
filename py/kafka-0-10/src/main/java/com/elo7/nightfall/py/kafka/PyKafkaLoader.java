package com.elo7.nightfall.py.kafka;

import com.elo7.nightfall.di.ExecutionMode;
import com.elo7.nightfall.di.Nightfall;
import com.elo7.nightfall.di.NightfallApplication;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQueryException;

@Nightfall(
		value = ExecutionMode.BATCH,
		scanPackages = {"com.elo7.nightfall.py.kafka", "com.elo7.nightfall.di.providers.kafka"})
public class PyKafkaLoader {

	private static SparkSession session;

	public static void init(SparkSession session, String[] args) {
		PyKafkaLoader.session = session;
		NightfallApplication.run(PyKafkaLoader.class, args);
	}

	public static Dataset<Row> load() {
		return PyExecutor.load();
	}

	public static void sink(Dataset<?> dataset) {
		PyExecutor.sink(dataset, null);
	}

	public static void sink(Dataset<?> dataset, String configPrefix) {
		PyExecutor.sink(dataset, configPrefix);
	}

	public static void start() {
		try {
			session.streams().awaitAnyTermination();
		} catch (StreamingQueryException e) {
			throw new RuntimeException(e);
		}
	}

	static SparkSession sparkSession() {
		return PyKafkaLoader.session;
	}
}
