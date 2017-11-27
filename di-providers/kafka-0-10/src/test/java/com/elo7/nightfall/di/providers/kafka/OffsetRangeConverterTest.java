package com.elo7.nightfall.di.providers.kafka;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class OffsetRangeConverterTest {

	private static final String STARTING_OFFSET = "{\"test\":{\"1\":23}}";
	private static final String STARTING_OFFSET_OTHER = "{\"test2\":{\"1\":23}}";
	private static final String ENDING_OFFSET = "{\"test\":{\"1\":26}}";

	private OffsetRangeConverter subject = new OffsetRangeConverter();

	@Test
	public void shouldReturnEmptyListWhenStartingAndEndingOffsetsAreNull() {
		List<OffsetRange> result = subject.apply(null, null);

		assertThat(result, is(empty()));
	}

	@Test
	public void shouldReturnEmptyListWhenStartingAndEndingOffsetsAreEmpty() {
		List<OffsetRange> result = subject.apply(" ", " ");

		assertThat(result, is(empty()));
	}

	@Test
	public void shouldIncludeTopicFromEndingOffset() {
		List<OffsetRange> result = subject.apply(null, ENDING_OFFSET);

		assertEquals("test", result.get(0).topic());
	}

	@Test
	public void shouldIncludePartitionFromEndingOffset() {
		List<OffsetRange> result = subject.apply(null, ENDING_OFFSET);

		assertEquals(1, result.get(0).partition());
	}

	@Test
	public void shouldIncludeUntilOffsetFromEndingOffset() {
		List<OffsetRange> result = subject.apply(null, ENDING_OFFSET);

		assertEquals(26, result.get(0).untilOffset());
	}

	@Test
	public void shouldIncludeDefaultFromOffsetWhenStartingOffsetIsNull() {
		List<OffsetRange> result = subject.apply(null, ENDING_OFFSET);

		assertEquals(0, result.get(0).fromOffset());
	}

	@Test
	public void shouldIncludeDataFromEndingOffsetOnly() {
		OffsetRange expected = new OffsetRange("test", 1, 0, 26);
		List<OffsetRange> result = subject.apply(STARTING_OFFSET_OTHER, ENDING_OFFSET);

		assertThat(result, contains(expected));
	}

	@Test
	public void shouldIncludeFromOffsetFromStartingOffset() {
		List<OffsetRange> result = subject.apply(STARTING_OFFSET, ENDING_OFFSET);

		assertEquals(23, result.get(0).fromOffset());
	}
}