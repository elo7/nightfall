package com.elo7.nightfall.di.providers.kafka.simple;

import com.elo7.nightfall.di.providers.kafka.InvalidTopicConfigurationException;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class KafkaSimpleConfigurationTest {

	private KafkaSimpleConfiguration subject;

	@Before
	public void setup() {
		subject = new KafkaSimpleConfiguration();
	}

	@Test(expected = InvalidTopicConfigurationException.class)
	public void shouldThrowInvalidTopicConfigurationExceptionWhenTopicsIsInvalid() {
		subject.setTopics(",,");
		subject.getTopics();
		fail();
	}

	@Test
	public void shouldReturnTopicSetWhenTopicsIsValid() {
		subject.setTopics("topic-a,topic-b");
		Set<String> topics = subject.getTopics();

		assertFalse(topics.isEmpty());
		assertTrue(topics.contains("topic-a"));
		assertTrue(topics.contains("topic-b"));
	}
}
