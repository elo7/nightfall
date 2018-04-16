package com.elo7.nightfall.py.kafka;

import com.elo7.nightfall.di.ExecutionMode;
import com.elo7.nightfall.di.Nightfall;
import com.elo7.nightfall.di.NightfallApplication;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
@Nightfall(
		value = ExecutionMode.BATCH,
		scanPackages = {"com.elo7.nightfall.py.kafka", "com.elo7.nightfall.di.providers.kafka"})
public class PyKafkaLoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(PyKafkaLoader.class);

	public static void init(SparkSession session, String[] args) {
		// Setup PySparkSessionProvider
		List<String> argsList = new ArrayList<>(Arrays.asList(args));
		argsList.add("-c");
		argsList.add(PySparkSessionProvider.class.getName());

		PySparkSessionProvider.session = session;
		NightfallApplication.run(PyKafkaLoader.class, argsList.toArray(new String[0]));
		LOGGER.info("Nightfall loaded!");
	}

	public static Dataset<Row> load() {
		return PyKafkaExecutor.load();
	}

	public static void sink(Dataset<?> dataset) {
		PyKafkaExecutor.sink(dataset, null);
	}

	public static void sink(Dataset<?> dataset, String configPrefix) {
		PyKafkaExecutor.sink(dataset, configPrefix);
	}

	public static void start() {
		try {
			PySparkSessionProvider.session.streams().awaitAnyTermination();
		} catch (StreamingQueryException e) {
			throw new RuntimeException(e);
		}
	}
}
