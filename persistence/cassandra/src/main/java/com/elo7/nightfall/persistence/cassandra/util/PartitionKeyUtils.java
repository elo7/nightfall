package com.elo7.nightfall.persistence.cassandra.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PartitionKeyUtils {

	private PartitionKeyUtils() {
	}

	public static List<UUID> resolvePartitionKeysForRangeByDay(LocalDate startDay, LocalDate endDay) {
		if (startDay.isEqual(endDay)) {
			return Collections.singletonList(TimeUUID.uuidForLocalDate(startDay));
		}
		return buildUUIDsForDaysInterval(startDay, endDay);
	}

	public static List<String> getDateInterval(LocalDate begin, LocalDate end) {
		if (begin.isEqual(end)) {
			return Collections.singletonList(begin.format(DateTimeFormatter.ISO_DATE));
		}
		long numberRepetitions = ChronoUnit.DAYS.between(begin, end);
		List<String> uuids = new ArrayList<>();
		for (long add = 0; add <= numberRepetitions; add++) {
			uuids.add(begin.plusDays(add).format(DateTimeFormatter.ISO_DATE));
		}
		return uuids;
	}

	private static List<UUID> buildUUIDsForDaysInterval(LocalDate startDay, LocalDate endDay) {
		long numberRepetitions = ChronoUnit.DAYS.between(startDay, endDay);
		List<UUID> uuids = new ArrayList<>();
		for (long add = 0; add <= numberRepetitions; add++) {
			uuids.add(TimeUUID.uuidForLocalDate(startDay.plusDays(add)));
		}
		return uuids;
	}

}
