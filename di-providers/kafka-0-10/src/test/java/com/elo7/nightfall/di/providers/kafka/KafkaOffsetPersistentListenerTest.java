package com.elo7.nightfall.di.providers.kafka;

import org.apache.spark.sql.streaming.SourceProgress;
import org.apache.spark.sql.streaming.StreamingQueryListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class KafkaOffsetPersistentListenerTest {

	private static final String APPLICATION = "appName";

	private KafkaOffsetPersistentListener subject;

	@Mock
	private KafkaOffsetRepository repository;
	@Mock
	private OffsetRangeConverter offsetRangeConverter;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private StreamingQueryListener.QueryProgressEvent queryProgressEvent;
	@Mock
	private SourceProgress sourceProgress;

	@Before
	public void setup() {
		subject = new KafkaOffsetPersistentListener(repository, APPLICATION, offsetRangeConverter);

		when(queryProgressEvent.progress().sources()).thenReturn(new SourceProgress[]{sourceProgress});
		when(offsetRangeConverter.apply("startOffset", "endOffset")).thenReturn(Collections.emptyList());
	}

	@Test
	public void shouldNotPersistWhenNumberOfInputRowsIsLessOrEqualsToZero() {
		subject.onQueryProgress(queryProgressEvent);

		verify(offsetRangeConverter, never()).apply(anyString(), anyString());
		verify(repository).persistTopics(Collections.emptyList(), APPLICATION);
	}

	@Test
	public void shouldNotPersistWhenNumberOfInputRowsHigherThanZeroAndSourceDescriptionIsNotFromKafka() {
		when(sourceProgress.numInputRows()).thenReturn(1L);
		when(sourceProgress.description()).thenReturn("OtherSource");

		subject.onQueryProgress(queryProgressEvent);

		verify(sourceProgress).description();
		verify(offsetRangeConverter, never()).apply(anyString(), anyString());
		verify(repository).persistTopics(Collections.emptyList(), APPLICATION);
	}

	@Test
	public void shouldPersistWhenNumberOfInputRowsHigherThanZeroAndSourceDescriptionIsFromKafka() {
		List<OffsetRange> offsetRanges = Collections.singletonList(new OffsetRange("topic", 1, 5, 10));
		when(sourceProgress.numInputRows()).thenReturn(1L);
		when(sourceProgress.description()).thenReturn("KafkaSource[topic]");
		when(sourceProgress.endOffset()).thenReturn("endOffset");
		when(sourceProgress.startOffset()).thenReturn("startOffset");
		when(offsetRangeConverter.apply("startOffset", "endOffset")).thenReturn(offsetRanges);

		subject.onQueryProgress(queryProgressEvent);

		verify(repository).persistTopics(offsetRanges, APPLICATION);
	}

	@Test
	public void shouldPersistOnlyRangesThatUntilOffsetIsHigherThanFromOffsetAndNumberOfInputRowsHigherThanZeroAndSourceDescriptionIsFromKafka() {
		List<OffsetRange> offsetRanges = Arrays.asList(
				new OffsetRange("topic", 1, 5, 10),
				new OffsetRange("topic2", 1, 5, 5));
		when(sourceProgress.numInputRows()).thenReturn(1L);
		when(sourceProgress.description()).thenReturn("KafkaSource[topic]");
		when(sourceProgress.endOffset()).thenReturn("endOffset");
		when(sourceProgress.startOffset()).thenReturn("startOffset");
		when(offsetRangeConverter.apply("startOffset", "endOffset")).thenReturn(offsetRanges);

		subject.onQueryProgress(queryProgressEvent);

		List<OffsetRange> expected = Collections.singletonList(new OffsetRange("topic", 1, 5, 10));
		verify(repository).persistTopics(expected, APPLICATION);
	}
}