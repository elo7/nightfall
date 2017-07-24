package com.elo7.nightfall.di.providers.reporter.statsd;

import org.apache.spark.sql.streaming.StreamingQueryListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StatsDReporterTest {

	@InjectMocks
	private StatsDReporter subject;
	@Mock
	private StatsD statsD;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private StreamingQueryListener.QueryProgressEvent event;

	@Test
	public void shouldSendMetricForInputRowsPerSecondOnQueryProgress() {
		double rows = 1.1;
		when(event.progress().inputRowsPerSecond()).thenReturn(rows);
		subject.onQueryProgress(event);

		verify(statsD).gauges("inputRowsPerSecond", 1L);
	}

	@Test
	public void shouldSendMetricForProcessedRowsPerSecondOnQueryProgress() {
		double rows = 1.1;
		when(event.progress().processedRowsPerSecond()).thenReturn(rows);
		subject.onQueryProgress(event);

		verify(statsD).gauges("processedRowsPerSecond", 1L);
	}

	@Test
	public void shouldSendMetricForNumInputRowsOnQueryProgress() {
		when(event.progress().numInputRows()).thenReturn(1L);
		subject.onQueryProgress(event);

		verify(statsD).gauges("numInputRows", 1L);
	}

	@Test
	public void shouldSendMetricForAllDurationsOnQueryProgress() {
		Map<String, Long> durations = new HashMap<>();

		durations.put("first", 1L);
		durations.put("second", 2L);

		when(event.progress().durationMs()).thenReturn(durations);
		subject.onQueryProgress(event);

		verify(statsD).timing("duration.first", 1L);
		verify(statsD).timing("duration.second", 2L);
	}
}