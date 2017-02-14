package com.elo7.nightfall.di.providers.kafka.topics;

import com.elo7.nightfall.di.Component;
import kafka.common.TopicAndPartition;
import org.apache.spark.streaming.kafka.KafkaCluster;
import org.apache.spark.streaming.kafka.OffsetRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.collection.JavaConversions;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class KafkaTopics implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaTopics.class);
	private final KafkaCluster kafkaCluster;

	@Inject
	KafkaTopics(KafkaCluster kafkaCluster) {
		this.kafkaCluster = kafkaCluster;
	}

	public Map<TopicAndPartition, OffsetRange> findOffsetRanges(Set<String> topics) {
		LOGGER.debug("Finding topic partitions");
		scala.collection.immutable.Set<TopicAndPartition> partitions =
				kafkaCluster.getPartitions(JavaConversions.asScalaSet(topics).toSet()).right().get();

		LOGGER.debug("Finding topics/partitions largest offsets");
		scala.collection.immutable.Map<TopicAndPartition, KafkaCluster.LeaderOffset> largestOffset =
				kafkaCluster.getLatestLeaderOffsets(partitions).right().get();

		LOGGER.debug("Finding topics/partitions smallest offsets");
		scala.collection.immutable.Map<TopicAndPartition, KafkaCluster.LeaderOffset> smallestOffset =
				kafkaCluster.getEarliestLeaderOffsets(partitions).right().get();

		Map<TopicAndPartition, KafkaCluster.LeaderOffset> largestMap = JavaConversions.mapAsJavaMap(largestOffset
				.toMap(scala.Predef$.MODULE$.<scala.Tuple2<TopicAndPartition, KafkaCluster.LeaderOffset>>conforms()));

		return largestMap
				.entrySet()
				.stream()
				.collect(Collectors.toMap(Map.Entry::getKey, entry -> convertToOffsetRange(smallestOffset, entry)));
	}

	private OffsetRange convertToOffsetRange(
			scala.collection.immutable.Map<TopicAndPartition, KafkaCluster.LeaderOffset> smallestOffset,
			Map.Entry<TopicAndPartition, KafkaCluster.LeaderOffset> largestOffset) {

		TopicAndPartition topicAndPartition = largestOffset.getKey();
		long fromOffset = smallestOffset.get(topicAndPartition).get().offset();

		return new OffsetRange(
				topicAndPartition.topic(),
				topicAndPartition.partition(),
				fromOffset,
				largestOffset.getValue().offset());
	}
}
