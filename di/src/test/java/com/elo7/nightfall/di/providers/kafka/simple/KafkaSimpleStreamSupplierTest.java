package com.elo7.nightfall.di.providers.kafka.simple;

import com.elo7.nightfall.di.providers.kafka.TopicNotFoundException;
import com.elo7.nightfall.di.providers.kafka.topics.KafkaTopicRepository;
import com.elo7.nightfall.di.providers.kafka.topics.KafkaTopics;
import kafka.common.TopicAndPartition;
import org.apache.spark.streaming.kafka.OffsetRange;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anySet;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class KafkaSimpleStreamSupplierTest {

    private final String APPLICATION = "application";
    @Mock
    private KafkaSimpleConfiguration configuration;
    @Mock
    private KafkaTopicRepository topicRepository;
    @Mock
    private KafkaTopics kafkaTopics;
    @InjectMocks
    private KafkaSimpleStreamSupplier subject;

    @Before
    public void setup() {
        when(configuration.getKafkaAutoOffsetReset()).thenReturn(KafkaAutoOffsetReset.LARGEST);
    }

    @Test(expected = TopicNotFoundException.class)
    @SuppressWarnings("unchecked")
    public void shouldReturnThrowExceptionWhenKafkaTopicsIsEmpty() {
        TopicAndPartition topicAndPartition = new TopicAndPartition("Persistent", 1);
        Map<TopicAndPartition, Long> persistentMap = buildPersistedMapMap(topicAndPartition, 1L);

        when(topicRepository.findTopicOffset(anySet(), anyString())).thenReturn(persistentMap);
        when(kafkaTopics.findOffsetRanges(anySet())).thenReturn(Collections.emptyMap());

        subject.findTopicOffset(APPLICATION);
        fail();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnKafkaTopicsOffsetRangeOnlyWhenPersistentTopicsIsEmpty() {
        TopicAndPartition topicAndPartition = new TopicAndPartition("Kafka", 1);
        Map<TopicAndPartition, OffsetRange> kafkaMap = buildKafkaMapMap(topicAndPartition, 1L, 1L);

        when(topicRepository.findTopicOffset(anySet(), anyString())).thenReturn(Collections.emptyMap());
        when(kafkaTopics.findOffsetRanges(anySet())).thenReturn(kafkaMap);

        Map<TopicAndPartition, Long> result = subject.findTopicOffset(APPLICATION);
        assertTrue(result.containsKey(topicAndPartition));
        assertEquals(1, result.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnKafkaTopicsOffsetRangeOnlyWhenPersistentDoNotExistsInKafka() {
        TopicAndPartition kafkaTopicAndPartition = new TopicAndPartition("Kafka", 1);
        TopicAndPartition persistentTopicAndPartition = new TopicAndPartition("Persistent", 1);
        Map<TopicAndPartition, OffsetRange> kafkaMap = buildKafkaMapMap(kafkaTopicAndPartition, 1L, 1L);
        Map<TopicAndPartition, Long> persistentMap = buildPersistedMapMap(persistentTopicAndPartition, 1L);

        when(topicRepository.findTopicOffset(anySet(), anyString())).thenReturn(persistentMap);
        when(kafkaTopics.findOffsetRanges(anySet())).thenReturn(kafkaMap);

        Map<TopicAndPartition, Long> result = subject.findTopicOffset(APPLICATION);
        assertTrue(result.containsKey(kafkaTopicAndPartition));
        assertFalse(result.containsKey(persistentTopicAndPartition));
        assertEquals(1, result.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnMergedTopicsOffsetRangeWithoutDuplicatedTopicsWhenPersistentAndKafkaHasSameTheTopic() {
        TopicAndPartition kafkaTopicAndPartition = new TopicAndPartition("Kafka", 1);
        TopicAndPartition persistentTopicAndPartition = new TopicAndPartition("Persistent", 1);
        Map<TopicAndPartition, OffsetRange> kafkaMap = buildKafkaMapMap(kafkaTopicAndPartition, 1L, 1L);
        Map<TopicAndPartition, Long> persistentMap = buildPersistedMapMap(persistentTopicAndPartition, 1L);
        kafkaMap.put(persistentTopicAndPartition,
                new OffsetRange(kafkaTopicAndPartition.topic(), kafkaTopicAndPartition.partition(), 1L, 5L));

        when(topicRepository.findTopicOffset(anySet(), anyString())).thenReturn(persistentMap);
        when(kafkaTopics.findOffsetRanges(anySet())).thenReturn(kafkaMap);

        Map<TopicAndPartition, Long> result = subject.findTopicOffset(APPLICATION);
        assertTrue(result.containsKey(kafkaTopicAndPartition));
        assertTrue(result.containsKey(persistentTopicAndPartition));
        assertEquals(new Long(1), result.get(persistentTopicAndPartition));
        assertEquals(2, result.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnTopicsOffsetRangeWithKafkaTopicsWhenPersistentIsLessThenKafkaFromOffset() {
        TopicAndPartition kafkaTopicAndPartition = new TopicAndPartition("Topic", 1);
        TopicAndPartition persistentTopicAndPartition = new TopicAndPartition("Topic", 1);
        Map<TopicAndPartition, OffsetRange> kafkaMap = buildKafkaMapMap(kafkaTopicAndPartition, 10L, 20L);
        Map<TopicAndPartition, Long> persistentMap = buildPersistedMapMap(persistentTopicAndPartition, 5L);

        when(topicRepository.findTopicOffset(anySet(), anyString())).thenReturn(persistentMap);
        when(kafkaTopics.findOffsetRanges(anySet())).thenReturn(kafkaMap);

        Map<TopicAndPartition, Long> result = subject.findTopicOffset(APPLICATION);
        assertTrue(result.containsKey(kafkaTopicAndPartition));
        assertTrue(result.containsKey(persistentTopicAndPartition));
        assertEquals(new Long(20), result.get(persistentTopicAndPartition));
        assertEquals(1, result.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnTopicsOffsetRangeWithKafkaTopicsWhenPersistentIsHigherThenKafkaUntilOffset() {
        TopicAndPartition kafkaTopicAndPartition = new TopicAndPartition("Topic", 1);
        TopicAndPartition persistentTopicAndPartition = new TopicAndPartition("Topic", 1);
        Map<TopicAndPartition, OffsetRange> kafkaMap = buildKafkaMapMap(kafkaTopicAndPartition, 1L, 10L);
        Map<TopicAndPartition, Long> persistentMap = buildPersistedMapMap(persistentTopicAndPartition, 25L);

        when(topicRepository.findTopicOffset(anySet(), anyString())).thenReturn(persistentMap);
        when(kafkaTopics.findOffsetRanges(anySet())).thenReturn(kafkaMap);

        Map<TopicAndPartition, Long> result = subject.findTopicOffset(APPLICATION);
        assertTrue(result.containsKey(kafkaTopicAndPartition));
        assertTrue(result.containsKey(persistentTopicAndPartition));
        assertEquals(new Long(10), result.get(persistentTopicAndPartition));
        assertEquals(1, result.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnTopicsOffsetRangeWithKafkaTopicsWhenPersistentOutOfRangeAndOffsetIsSmallest() {
        TopicAndPartition kafkaTopicAndPartition = new TopicAndPartition("Topic", 1);
        TopicAndPartition persistentTopicAndPartition = new TopicAndPartition("Topic", 1);
        Map<TopicAndPartition, OffsetRange> kafkaMap = buildKafkaMapMap(kafkaTopicAndPartition, 35L, 55L);
        Map<TopicAndPartition, Long> persistentMap = buildPersistedMapMap(persistentTopicAndPartition, 25L);

        when(topicRepository.findTopicOffset(anySet(), anyString())).thenReturn(persistentMap);
        when(kafkaTopics.findOffsetRanges(anySet())).thenReturn(kafkaMap);
        when(configuration.getKafkaAutoOffsetReset()).thenReturn(KafkaAutoOffsetReset.SMALLEST);

        Map<TopicAndPartition, Long> result = subject.findTopicOffset(APPLICATION);
        assertTrue(result.containsKey(kafkaTopicAndPartition));
        assertTrue(result.containsKey(persistentTopicAndPartition));
        assertEquals(new Long(35), result.get(persistentTopicAndPartition));
        assertEquals(1, result.size());
    }

    private Map<TopicAndPartition, Long> buildPersistedMapMap(TopicAndPartition topicAndPartition, Long offset) {
        Map<TopicAndPartition, Long> offsetRange = new HashMap<>();
        offsetRange.put(topicAndPartition, offset);

        return offsetRange;
    }

    private Map<TopicAndPartition, OffsetRange> buildKafkaMapMap(TopicAndPartition topic, long from, long until) {
        Map<TopicAndPartition, OffsetRange> offsetRange = new HashMap<>();
        offsetRange.put(
                topic,
                new OffsetRange(topic.topic(), topic.partition(), from, until));

        return offsetRange;
    }
}
