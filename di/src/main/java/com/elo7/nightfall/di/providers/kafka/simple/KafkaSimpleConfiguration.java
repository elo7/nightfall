package com.elo7.nightfall.di.providers.kafka.simple;

import com.elo7.nightfall.di.providers.kafka.InvalidTopicConfigurationException;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import com.netflix.governator.annotations.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Kafka simple configurations:
 * <ul>
 * <li>kafka.brokers: list of kafka brokers, format: address:port,address:port. Example: kafka-1:9092,kafka-2,9092.</li>
 * <li>kafka.topics: list of topics, format: topic-A,topic-B.</li>
 * </ul>
 */
public class KafkaSimpleConfiguration implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank
	@Configuration("kafka.brokers")
	private String brokers;
	@NotBlank
	@Configuration("kafka.topics")
	private String topics;
	@Configuration("kafka.offset.persistent")
	private Boolean persistentTopicOffsets = false;
	@Configuration("kafka.auto.offset.reset")
	private String kafkaAutoOffsetReset = "largest";

	public Set<String> getTopics() {
		Set<String> topicSet = Stream.of(topics.split(","))
				.filter(StringUtils::isNotBlank)
				.collect(Collectors.toSet());

		if (topicSet.isEmpty()) {
			throw new InvalidTopicConfigurationException(
					"Invalid topic configuration, expected topic,topic, but found: " + topics);
		}

		return topicSet;
	}

	@VisibleForTesting
	void setTopics(String topics) {
		this.topics = topics;
	}

	public Map<String, String> params() {
		return ImmutableMap.of("metadata.broker.list", brokers);
	}

	public Boolean isPersistentTopicOffsets() {
		return persistentTopicOffsets;
	}

	public KafkaAutoOffsetReset getKafkaAutoOffsetReset() {
		return KafkaAutoOffsetReset.valueOf(kafkaAutoOffsetReset.toUpperCase());
	}
}
