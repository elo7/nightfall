package com.elo7.nightfall.di.providers.kafka;

import com.elo7.nightfall.di.NightfallConfigurations;
import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.Serializable;

public class KafkaFactory implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Dataset<Row> load(SparkSession session, NightfallConfigurations configurations, String configPrefix) {
		DataFrameReader reader = session
				.read()
				.format("kafka");

		configurations
				.getPropertiesWithPrefix(configPrefix)
				.entrySet()
				.forEach(entry -> reader.option(entry.getKey().replaceFirst(configPrefix, ""), entry.getValue()));

		return reader.load();
	}
}
