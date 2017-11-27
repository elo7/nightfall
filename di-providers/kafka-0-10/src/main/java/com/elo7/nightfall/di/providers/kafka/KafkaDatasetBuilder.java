package com.elo7.nightfall.di.providers.kafka;

import com.elo7.nightfall.di.commons.json.JsonParser;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.kafka010.KafkaSource;
import org.apache.spark.sql.kafka010.KafkaSourceOffset;
import org.apache.spark.sql.streaming.DataStreamReader;
import org.apache.spark.sql.streaming.StreamingQueryListener;

import javax.swing.text.html.Option;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KafkaDatasetBuilder {

	private final SparkSession session;
	private final Map<String, String> configurations;
	private KafkaOffsetRepository repository;
	private StreamingQueryListener listener;

	public KafkaDatasetBuilder(SparkSession session, Map<String, String> configurations) {
		this.session = session;
		this.configurations = configurations;
	}

	public KafkaDatasetBuilder withPersistentOffsets(KafkaOffsetRepository repository, KafkaOffsetPersistentListener listener) {
		this.repository = repository;
		this.listener = listener;
		return this;
	}

	public Dataset<Row> build() {
		DataStreamReader kafka = session
				.readStream()
				.format("kafka");

		// Kafka configuration
		configurations.forEach(kafka::option);

		if (repository != null) {
			Set<String> topics = getTopics();
			// Sanity checks
			if (topics.isEmpty()) {
				throw new UnsupportedOperationException("Persistent offset is only supported with subscribe");
			}

			startingOffset(topics).ifPresent(offsets -> kafka.option("startingOffsets", offsets));
			// Register Kafka Listener
			session.streams().addListener(listener);
		}

		return kafka.load();
	}

	private Optional<String> startingOffset(Set<String> topics){
		boolean startingOffsetFromRepository = BooleanUtils.toBoolean(configurations.get("persistent.startingOffsets.fromRepository"));

		if (!startingOffsetFromRepository || StringUtils.isNotBlank(configurations.get("startingOffsets"))){
			return Optional.empty();
		}

		Map<String, Map<String, Long>> offsets = repository.findTopicOffset(topics, session.sparkContext().appName());

		if (offsets.isEmpty()) {
			return Optional.empty();
		}

		return Optional.ofNullable(JsonParser.toJson(offsets));
	}


	private Set<String> getTopics() {
		String topicList = configurations.get("subscribe");

		if (StringUtils.isBlank(topicList)) {
			return Collections.emptySet();
		}

		return Stream.of(topicList.split(",")).map(String::trim).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
	}
}
