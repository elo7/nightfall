package com.elo7.nightfall.di.providers.kafka.highlevel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.elo7.nightfall.di.providers.kafka.InvalidTopicConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

import com.google.common.annotations.VisibleForTesting;
import com.netflix.governator.annotations.Configuration;

/**
 * Kafka High Level configurations parameters:
 * <ul>
 * <li>kafka.group: group name to bind in Kafka.</li>
 * <li>kafka.topics.map: topics, format: topic:partitions, partitions is optional, when not set will use the one
 * defined on <b>kafka.default.topic.partitions</b>. Examples:
 * <ul>
 * <li>topic-A:2,topic-B:1</li>
 * <li>topic-A,topic-B,topic-C</li>
 * </ul>
 * </li>
 * <li>kafka.zookeeper: zookeeper quorum, format: address:port,address:port/chroot. Example: zoo-1:2181,zoo-2:2181/kafka.</li>
 * <li>kafka.default.topic.partitions: number of partitions for kafka maps when not set. Default 1.</li>
 * </ul>
 */
public class KafkaHighLevelConfiguration implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank
    @Configuration("kafka.group")
    private String groupId;
    @NotBlank
    @Configuration("kafka.topics.map")
    private String topicsMap;
    @NotBlank
    @Configuration("kafka.zookeeper")
    private String zookeeper;
    @Configuration("kafka.default.topic.partitions")
    private Integer defaultPartitions = 1;

    public String getGroupId() {
        return groupId;
    }

    public String getZookeeper() {
        return zookeeper;
    }

    public Map<String, Integer> getTopicsMap() {
        Map<String, Integer> topicMap = new HashMap<>();

        for (String topic : topicsMap.split(",")) {
            String[] topicItem = topic.split(":");

            if (topicItem.length > 2) {
                throw new InvalidTopicConfigurationException("Expected TOPIC_NAME[:PARTITIONS], but found: " + topic);
            } else if (topicItem.length == 2) {
                validateTopicName(topicItem[0]);
                topicMap.put(topicItem[0], Integer.parseInt(topicItem[1]));
            } else {
                validateTopicName(topicItem[0]);
                topicMap.put(topicItem[0], defaultPartitions);
            }

        }

        return topicMap;
    }

    private void validateTopicName(String topic) {
        if (StringUtils.isBlank(topic)) {
            throw new InvalidTopicConfigurationException("Topic name cannot be empty, found: " + topic);
        }
    }

    @VisibleForTesting
    void setTopicsMap(String topicsMap) {
        this.topicsMap = topicsMap;
    }

    @VisibleForTesting
    void setDefaultPartitions(Integer defaultPartitions) {
        this.defaultPartitions = defaultPartitions;
    }
}
