package com.elo7.nightfall.distcp.configuration;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NightfallDistcpConfigurationTest {

	private static final LocalDate END_DATE = LocalDate.now();
	private static final YearMonth NOW = YearMonth.now();

	private NightfallDistcpConfiguration subject;

	@Before
	public void setup() {
		subject = new NightfallDistcpConfiguration();
		subject.windowDateEndStr = END_DATE.toString();
	}

	@Test
	public void shouldReturnEndDateInclusiveForAnyWindowSize() {
		subject.windowSizeInDays = 1;
		subject.type = DistcpType.DAY.name();

		Set<LocalDate> dates = subject.getWindowDays();

		assertTrue(dates.contains(END_DATE));
	}

	@Test
	public void shouldReturnDatesSizeEqualToWindowSize() {
		int windowSize = 3;
		subject.windowSizeInDays = windowSize;
		subject.type = DistcpType.DAY.name();

		Set<LocalDate> dates = subject.getWindowDays();

		assertEquals(windowSize, dates.size());
	}

	@Test
	public void lengtOfSetMustBeEqualsOfLengthOfMonth() {
		subject.type = DistcpType.MONTH.name();
		Set<LocalDate> dates = subject.getWindowDays();
		assertEquals(NOW.lengthOfMonth(), dates.size());
	}

	@Test
	public void firstElementOfSetMustBeFirstDayOfMonth() {
		subject.type = DistcpType.MONTH.name();
		Set<LocalDate> dates = subject.getWindowDays();

		LocalDate firstDayOfMonth = (LocalDate) dates.toArray()[0];

		assertEquals(NOW.atDay(1), firstDayOfMonth);
	}

	@Test
	public void lastElementOfSetMustBeLastDayOfMonth() {
		subject.type = DistcpType.MONTH.name();
		Set<LocalDate> dates = subject.getWindowDays();

		LocalDate lastDayOfMonth = (LocalDate) dates.toArray()[dates.size() - 1];

		assertEquals(NOW.atEndOfMonth(), lastDayOfMonth);
	}

}
