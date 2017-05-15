package com.elo7.nightfall.di.providers.kafka.simple;

import com.elo7.nightfall.di.providers.kafka.TopicNotFoundException;
import com.elo7.nightfall.di.providers.kafka.topics.KafkaTopicRepository;
import com.elo7.nightfall.di.providers.kafka.topics.KafkaTopics;
import com.elo7.nightfall.di.providers.spark.stream.StreamSupplier;
import com.google.common.annotations.VisibleForTesting;
import kafka.common.TopicAndPartition;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.apache.spark.streaming.kafka.OffsetRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import javax.inject.Inject;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class KafkaSimpleStreamSupplier implements StreamSupplier {

	private static final long serialVersionUID = 1L;
	private static final Long DEFAULT_RANGE = -1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSimpleStreamSupplier.class);

	private final KafkaSimpleConfiguration configuration;
	private final KafkaTopicRepository topicRepository;
	private final KafkaTopics kafkaTopics;

	@Inject
	KafkaSimpleStreamSupplier(
			KafkaSimpleConfiguration configuration,
			KafkaTopicRepository topicRepository,
			KafkaTopics kafkaTopics) {
		this.configuration = configuration;
		this.topicRepository = topicRepository;
		this.kafkaTopics = kafkaTopics;
	}

	@Override
	public JavaDStream<String> get(JavaStreamingContext context) {
		if (configuration.isPersistentTopicOffsets()) {
			Map<TopicAndPartition, Long> offsetRange = findTopicOffset(context.sparkContext().appName());

			return KafkaUtils.createDirectStream(
					context,
					String.class,
					String.class,
					StringDecoder.class,
					StringDecoder.class,
					String.class,
					configuration.params(),
					offsetRange,
					MessageAndMetadata::message);
		}

		return KafkaUtils.createDirectStream(
				context,
				String.class,
				String.class,
				StringDecoder.class,
				StringDecoder.class,
				configuration.params(),
				configuration.getTopics())
				.map(Tuple2::_2);
	}

	@VisibleForTesting
	Map<TopicAndPartition, Long> findTopicOffset(String application) {
		LOGGER.debug("Loading offset from database");
		Map<TopicAndPartition, Long> persistedOffsetRange = topicRepository.findTopicOffset(
				configuration.getTopics(), application);
		LOGGER.debug("Loading offset range from Kafka");
		Map<TopicAndPartition, OffsetRange> kafkaOffsetRanges = kafkaTopics.findOffsetRanges(configuration.getTopics());

		if (kafkaOffsetRanges.isEmpty()) {
			throw new TopicNotFoundException("Not topics found on Kafka matching topics: " + configuration.getTopics());
		}

		return kafkaOffsetRanges.entrySet().stream()
				.map(entry -> computeOffsetRange(entry, persistedOffsetRange.getOrDefault(entry.getKey(), DEFAULT_RANGE)))
				.collect(Collectors.toMap(Tuple2::_1, Tuple2::_2));
	}

	private Tuple2<TopicAndPartition, Long> computeOffsetRange(Entry<TopicAndPartition, OffsetRange> entry, Long offset) {
		OffsetRange kafkaOffsetRange = entry.getValue();
		boolean isNotValidOffset = offset < kafkaOffsetRange.fromOffset() || offset > kafkaOffsetRange.untilOffset();

		if (isNotValidOffset) {
			LOGGER.warn("Invalid offset {}, using kafka offset.", offset);
			switch (configuration.getKafkaAutoOffsetReset()) {
				case SMALLEST:
					offset = kafkaOffsetRange.fromOffset();
					break;
				case LARGEST:
					offset = kafkaOffsetRange.untilOffset();
					break;
			}
		}

		return new Tuple2<>(entry.getKey(), offset);
	}
}
