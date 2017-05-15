package com.elo7.nightfall.di.providers.kafka.simple;

import com.elo7.nightfall.di.function.Emiter;
import com.elo7.nightfall.di.providers.kafka.topics.KafkaTopicRepository;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.kafka.HasOffsetRanges;
import org.apache.spark.streaming.kafka.OffsetRange;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class KafkaSimplePostConsumer implements Emiter<JavaDStream<String>> {

	private static final long serialVersionUID = 2L;

	private final KafkaSimpleConfiguration configuration;
	private final KafkaTopicRepository topicRepository;

	@Inject
	KafkaSimplePostConsumer(
			KafkaSimpleConfiguration configuration,
			KafkaTopicRepository topicRepository) {
		this.configuration = configuration;
		this.topicRepository = topicRepository;
	}

	@Override
	public void accept(JavaDStream<String> kafkaStream) {
		if (configuration.isPersistentTopicOffsets()) {
			kafkaStream.foreachRDD(rdd -> {
				List<OffsetRange> validOffsets = Arrays.stream(((HasOffsetRanges) rdd.rdd()).offsetRanges())
						.filter(offset -> offset.fromOffset() != offset.untilOffset())
						.collect(Collectors.toList());

				topicRepository.persistTopics(validOffsets, kafkaStream.context().sparkContext().appName());
			});
		}
	}
}
