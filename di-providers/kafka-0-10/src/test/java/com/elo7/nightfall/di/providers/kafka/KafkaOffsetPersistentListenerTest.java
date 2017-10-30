package com.elo7.nightfall.di.providers.kafka;

import org.apache.spark.sql.streaming.SourceProgress;
import org.apache.spark.sql.streaming.StreamingQueryListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Mockito.anyList;
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
	@Mock
	private List<OffsetRange> offsetRanges;

	@Before
	public void setup() {
		subject = new KafkaOffsetPersistentListener(repository, APPLICATION, offsetRangeConverter);

		when(queryProgressEvent.progress().sources()).thenReturn(new SourceProgress[]{sourceProgress});
	}

	@Test
	public void shouldNotPersistWhenNumberOfInputRowsIsLessOrEqualsToZero() {
		subject.onQueryProgress(queryProgressEvent);

		verify(offsetRangeConverter, never()).apply(anyString(), anyString());
		verify(repository, never()).persistTopics(anyList(), anyString());
	}

	@Test
	public void shouldNotPersistWhenNumberOfInputRowsHigherThanZeroAndSourceDescriptionIsNotFromKafka() {
		when(sourceProgress.numInputRows()).thenReturn(1L);
		when(sourceProgress.description()).thenReturn("OtherSource");

		subject.onQueryProgress(queryProgressEvent);

		verify(sourceProgress).description();
		verify(offsetRangeConverter, never()).apply(anyString(), anyString());
		verify(repository, never()).persistTopics(anyList(), anyString());
	}

	@Test
	public void shouldPersistWhenNumberOfInputRowsHigherThanZeroAndSourceDescriptionIsFromKafka() {
		when(sourceProgress.numInputRows()).thenReturn(1L);
		when(sourceProgress.description()).thenReturn("KafkaSource[topic]");
		when(sourceProgress.endOffset()).thenReturn("endOffset");
		when(sourceProgress.startOffset()).thenReturn("startOffset");
		when(offsetRangeConverter.apply("startOffset", "endOffset")).thenReturn(offsetRanges);

		subject.onQueryProgress(queryProgressEvent);

		verify(repository).persistTopics(offsetRanges, APPLICATION);
	}
}