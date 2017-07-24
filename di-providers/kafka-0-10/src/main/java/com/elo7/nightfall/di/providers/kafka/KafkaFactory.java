package com.elo7.nightfall.di.providers.kafka;

import com.elo7.nightfall.di.NightfallConfigurations;
import com.elo7.nightfall.di.commons.json.JsonParser;
import com.elo7.nightfall.di.providers.kafka.cassandra.CassandraConfiguration;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.DataStreamReader;

import java.io.Serializable;
import java.util.Map;

public class KafkaFactory implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Dataset<Row> load(SparkSession session, NightfallConfigurations configurations, String configPrefix) {
		DataStreamReader kafka = session
				.readStream()
				.format("kafka");

		Map<String, String> kafkaConfigs = configurations.getPropertiesWithPrefix(configPrefix);

		kafkaConfigs
				.entrySet()
				.forEach(entry -> kafka.option(entry.getKey().replaceFirst(configPrefix, ""), entry.getValue()));

		boolean persistentOffsets = BooleanUtils.toBoolean(kafkaConfigs.get("persistent.offsets"));

		if (persistentOffsets) {
			// Sanity checks
			if (kafkaConfigs.containsKey("subscribePattern")) {
				throw new UnsupportedOperationException("Persistent offset is not supported for subscribePattern");
			}

			CassandraConfiguration cassandraConfiguration = CassandraConfiguration.build(kafkaConfigs);

			try (KafkaOffsetRepository repository = new KafkaOffsetRepository(cassandraConfiguration)) {
				Map<String, Map<String, Long>> offsets = repository.findTopicOffset(null, session.sparkContext().appName());

				if (!offsets.isEmpty()) {
					kafka.option("startingOffsets", JsonParser.toJson(offsets));
				}
			}

		}

		return kafka.load();
	}
}
