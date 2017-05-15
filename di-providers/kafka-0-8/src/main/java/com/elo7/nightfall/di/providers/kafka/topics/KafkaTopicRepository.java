package com.elo7.nightfall.di.providers.kafka.topics;

import kafka.common.TopicAndPartition;
import org.apache.spark.streaming.kafka.OffsetRange;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface KafkaTopicRepository extends Serializable {

	Map<TopicAndPartition, Long> findTopicOffset(Set<String> topics, String application);

	void persistTopics(List<OffsetRange> offsetRanges, String application);
}
