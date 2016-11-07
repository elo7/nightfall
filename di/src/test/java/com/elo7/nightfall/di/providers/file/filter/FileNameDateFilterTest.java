package com.elo7.nightfall.di.providers.file.filter;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class FileNameDateFilterTest {

	private FileNameDateFilter subject;
	private LocalDate date = LocalDate.parse("2016-05-15");

	@Before
	public void setup() {
		subject = new FileNameDateFilter();
		subject.windowDateEndStr = date.toString();
	}

	@Test
	public void shouldReturnSourceWithoutChangesWhenPatternIsNotWithinSource() {
		String source = "/folder/2020-05-15.gz";
		String result = subject.filterSource(source);

		assertEquals(source, result);
	}

	@Test
	public void shouldReturnLastThreeDaysWhenSlideWindowIsZeroAndWindowSizeIsThree() {
		subject.windowSizeInDays = 3;
		subject.windowSlideInDays = 0;

		String result = subject.filterSource("/folder/${DATE}.gz");
		assertEquals("/folder/2016-05-13.gz,/folder/2016-05-14.gz,/folder/2016-05-15.gz", result);
	}

	@Test
	public void shouldReturnFromFiveDaysUntilTwoDaysAgoWhenWindowSizeIsThreeAndSlideIsThree() {
		subject.windowSizeInDays = 3;
		subject.windowSlideInDays = 2;

		String result = subject.filterSource("/folder/${DATE}.gz");
		assertEquals("/folder/2016-05-11.gz,/folder/2016-05-12.gz,/folder/2016-05-13.gz", result);
	}

	@Test
	public void shouldReturnYesterdayWithDefaultValues() {
		String result = subject.filterSource("/folder/${DATE}.gz");

		assertEquals("/folder/2016-05-14.gz", result);
	}
}
