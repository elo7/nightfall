package com.elo7.nightfall.di.providers.kafka;

import com.elo7.nightfall.di.NightfallConfigurations;
import com.elo7.nightfall.di.commons.json.JsonParser;
import com.elo7.nightfall.di.providers.kafka.cassandra.CassandraConfiguration;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.DataStreamReader;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KafkaFactory implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Dataset<Row> load(SparkSession session, NightfallConfigurations configurations, String configPrefix) {
		Map<String, String> kafkaConfigs = configurations.getPropertiesWithPrefix(configPrefix);
		DataStreamReader kafka = session
				.readStream()
				.format("kafka");

		kafkaConfigs.forEach((key, value) -> kafka.option(key.replaceFirst(configPrefix, ""), value));
		boolean persistentOffsets = BooleanUtils.toBoolean(kafkaConfigs.get("persistent.offsets"));

		if (persistentOffsets) {
			Set<String> topics = getTopics(kafkaConfigs);
			// Sanity checks
			if (topics.isEmpty()) {
				throw new UnsupportedOperationException("Persistent offset is only supported with subscribe");
			}

			KafkaOffsetRepository repository = new KafkaOffsetRepository(CassandraConfiguration.build(kafkaConfigs));
			Map<String, Map<String, Long>> offsets = repository.findTopicOffset(topics, session.sparkContext().appName());

			if (!offsets.isEmpty()) {
				kafka.option("startingOffsets", JsonParser.toJson(offsets));
			}

			// Register Kafka Listener
			session.streams().addListener(new KafkaOffsetPersistentListener(repository));
		}

		return kafka.load();
	}

	private static Set<String> getTopics(Map<String, String> kafkaConfigs) {
		String topicList = kafkaConfigs.get("subscribe");

		if (StringUtils.isBlank(topicList)) {
			return Collections.emptySet();
		}

		return Stream.of(topicList.split(".")).collect(Collectors.toSet());
	}
}
