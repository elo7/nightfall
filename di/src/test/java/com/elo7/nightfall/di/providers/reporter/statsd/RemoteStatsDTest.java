package com.elo7.nightfall.di.providers.reporter.statsd;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RemoteStatsDTest {

	private RemoteStatsD subject;
	@Mock
	private UDPSender sender;
	private final String PREFIX = "remoteStatsD.";

	@Before
	public void setup() {
		subject = new RemoteStatsD(sender, 50, PREFIX);
	}

	@Test
	public void shouldNotSentWhenBufferIsNotFull() {
		subject.gauges("stat", 1);

		verify(sender, never()).send(anyString());
	}

	@Test
	public void shouldSentWhenBufferIsFull() {
		subject.gauges("sta.that.will.let.the.buffer.full", 1);
		subject.gauges("another.one", 1);

		verify(sender).send(anyString());
	}

	@Test
	public void shouldNotFlushWhenBufferIsEmpty() {
		subject.flush();

		verify(sender, never()).send(anyString());
	}

	@Test
	public void shouldFlushWhenBufferIsNotEmpty() {
		subject.gauges("stat", 1);
		subject.flush();

		verify(sender).send(anyString());
	}

	@Test
	public void shouldDiscardStatsHigherThanBufferSize() {
		subject.gauges("a.very.big.stat.that.is.higher.than.buffer.size.or.at.least.we.hope.so", 1);
		subject.flush();

		verify(sender, never()).send(anyString());
	}

	@Test
	public void shouldFormatTimingCorrectly() {
		subject.timing("timer", 2);
		subject.flush();

		verify(sender).send(PREFIX + "timer:2|ms\n");
	}

	@Test
	public void shouldFormatIncrementCorrectly() {
		subject.increment("inc");
		subject.flush();

		verify(sender).send(PREFIX + "inc:1|c\n");
	}

	@Test
	public void shouldFormatIncrementWithMagnitudeCorrectly() {
		subject.increment("inc", 2);
		subject.flush();

		verify(sender).send(PREFIX + "inc:2|c\n");
	}

	@Test
	public void shouldFormatSetsCorrectly() {
		subject.sets("sets", 4);
		subject.flush();

		verify(sender).send(PREFIX + "sets:4|s\n");
	}

	@Test
	public void shouldFormatGaugesCorrectly() {
		subject.gauges("gauge", 5);
		subject.flush();

		verify(sender).send(PREFIX + "gauge:5|g\n");
	}

	@Test
	public void shouldFormatMultiplesMetricsCorrectly() {
		subject.increment("inc1");
		subject.increment("inc2");
		subject.flush();

		verify(sender).send(PREFIX + "inc1:1|c\n" + PREFIX + "inc2:1|c\n");
	}
}