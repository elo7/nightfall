package com.elo7.nightfall.di.providers.kafka.highlevel;

import com.elo7.nightfall.di.providers.kafka.InvalidTopicConfigurationException;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class KafkaHighLevelConfigurationTest {

    private KafkaHighLevelConfiguration subject;

    @Before
    public void setup() {
        subject = new KafkaHighLevelConfiguration();
    }

    @Test(expected = InvalidTopicConfigurationException.class)
    public void shouldReturnEmptyMapWhenTopicsIsEmpty() {
        subject.setTopicsMap(" ");
        subject.getTopicsMap();
        fail();
    }

    @Test
    public void shouldReturnMapWhenTopicsIsValid() {
        subject.setTopicsMap("topic-a:1,topic-b:2");
        Map<String, Integer> topics = subject.getTopicsMap();

        assertFalse(topics.isEmpty());
        assertTrue(topics.containsKey("topic-a"));
        assertTrue(topics.containsKey("topic-b"));
        assertEquals(new Integer(1), topics.get("topic-a"));
        assertEquals(new Integer(2), topics.get("topic-b"));
    }

    @Test
    public void shouldReturnMapWithDefaultPartitionsWhenTopicsIsValid() {
        Integer defaultPartitions = 2;
        subject.setTopicsMap("topic-a,topic-b");
        subject.setDefaultPartitions(defaultPartitions);
        Map<String, Integer> topics = subject.getTopicsMap();

        assertFalse(topics.isEmpty());
        assertTrue(topics.containsKey("topic-a"));
        assertTrue(topics.containsKey("topic-b"));
        assertEquals(defaultPartitions, topics.get("topic-a"));
        assertEquals(defaultPartitions, topics.get("topic-b"));
    }

    @Test(expected = InvalidTopicConfigurationException.class)
    public void shouldThrowInvalidTopicConfigurationExceptionWhenTopicIsInvalid() {
        subject.setTopicsMap("topic-a:1:2,topic-b:2");
        subject.getTopicsMap();
        fail();
    }
}
