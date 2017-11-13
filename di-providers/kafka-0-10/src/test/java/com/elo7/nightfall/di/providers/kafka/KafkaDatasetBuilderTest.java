package com.elo7.nightfall.di.providers.kafka;

import com.google.common.collect.ImmutableMap;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.DataStreamReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anySet;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class KafkaDatasetBuilderTest {

	private KafkaDatasetBuilder subject;

	private static final String APP = "applicationName";

	private Map<String, String> configurations;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private SparkSession session;
	@Mock
	private KafkaOffsetRepository repository;
	@Mock
	private KafkaOffsetPersistentListener listener;
	@Mock
	private DataStreamReader kafka;

	@Before
	public void setup() {
		when(session.readStream().format("kafka")).thenReturn(kafka);
		when(session.sparkContext().appName()).thenReturn(APP);

		configurations = new HashMap<>();
		configurations.put("persistent.startingOffsets.fromRepository", "true");
		subject = new KafkaDatasetBuilder(session, configurations);
	}

	@Test
	public void shouldSetAllConfigurationsInDataStreamReader() {
		configurations.put("option1", "value1");
		configurations.put("option2", "value2");

		subject.build();

		verify(kafka).option("option1", "value1");
		verify(kafka).option("option2", "value2");
	}

	@Test
	public void shouldAddKafkaOffsetPersistentListenerWhenRepositoryIsNotNull() {
		configurations.put("subscribe", "topic1, topic2 ");

		subject.withPersistentOffsets(repository, listener).build();

		verify(session.streams()).addListener(listener);
	}

	@Test
	public void shouldNotAddKafkaOffsetPersistentListenerWhenRepositoryIsNull() {
		configurations.put("subscribe", "topic1, topic2 ");

		subject.withPersistentOffsets(null, listener).build();

		verify(session.streams(), never()).addListener(listener);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void shouldThrowUnsupportedOperationExceptionWhenRepositoryIsNotNullAndSubscribeOptionIsEmpty() {
		configurations.put("subscribe", " ");

		subject.withPersistentOffsets(repository, listener).build();

		fail();
	}

	@Test
	public void shouldRemoveTrimTopicNamesFromTopicListOfSubscribeOption() {
		Set<String> topics = Stream.of("topic1", "topic2").collect(Collectors.toSet());
		configurations.put("subscribe", "topic1, topic2 ");
		when(repository.findTopicOffset(anySet(), anyString())).thenReturn(Collections.emptyMap());

		subject.withPersistentOffsets(repository, listener).build();

		verify(repository).findTopicOffset(topics, APP);
	}

	@Test
	public void shouldUseTopicListAsStartingOffSetWhenIsNotBlank() {
		Set<String> topics = Stream.of("topic1", "topic2").collect(Collectors.toSet());
		configurations.put("subscribe", "topic1,topic2");
		when(repository.findTopicOffset(anySet(), anyString())).thenReturn(Collections.emptyMap());

		subject.withPersistentOffsets(repository, listener).build();

		verify(kafka).option("subscribe", "topic1,topic2");
		verify(repository).findTopicOffset(topics, APP);
	}

	@Test
	public void shouldNotSetStartingOffSetsWhenNoneIsFoundOnRepositoryAndOffsetFromRepositoryIsEnabled() {
		configurations.put("subscribe", "topic1,topic2");
		when(repository.findTopicOffset(anySet(), anyString())).thenReturn(Collections.emptyMap());

		subject.withPersistentOffsets(repository, listener).build();

		verify(kafka, never()).option(eq("startingOffsets"), any());
	}

	@Test
	public void shouldSetStartingOffSetsWhenIsFoundOnRepositoryAndOffsetFromRepositoryIsEnabled() {
		configurations.put("subscribe", "topic1,topic2");
		Map<String, Map<String, Long>> startingOffset = new HashMap<>();

		startingOffset.put("topic1", ImmutableMap.of("0", 1L));
		startingOffset.put("topic2", ImmutableMap.of("1", 11L));

		when(repository.findTopicOffset(anySet(), anyString())).thenReturn(startingOffset);

		subject.withPersistentOffsets(repository, listener).build();

		verify(kafka).option(eq("subscribe"), anyString());
		verify(kafka).option("startingOffsets", "{\"topic1\":{\"0\":1},\"topic2\":{\"1\":11}}");
	}

	@Test
	public void shouldNotSetStartingOffSetsWhenIsFoundOnRepositoryAndOffsetFromRepositoryIsDisabled() {
		configurations.put("subscribe", "topic1,topic2");
		configurations.put("persistent.startingOffsets.fromRepository", "false");

		subject.withPersistentOffsets(repository, listener).build();

		verify(repository, never()).findTopicOffset(anySet(), anyString());
		verify(kafka).option(eq("subscribe"), anyString());
		verify(kafka, never()).option(eq("startingOffsets"), anyString());
	}

}