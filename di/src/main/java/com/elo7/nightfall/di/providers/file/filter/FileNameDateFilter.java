package com.elo7.nightfall.di.providers.file.filter;

import com.elo7.nightfall.di.Component;
import com.elo7.nightfall.di.providers.file.FileFilter;
import com.google.common.annotations.VisibleForTesting;
import com.netflix.governator.annotations.Configuration;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
class FileNameDateFilter implements FileFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileNameDateFilter.class);
	private static final String DATE_TOKEN = "${DATE}";

	@VisibleForTesting
	@Min(1)
	@Configuration("file.filter.name.window.size.days")
	int windowSizeInDays = 1;
	@VisibleForTesting
	@Min(0)
	@Configuration("file.filter.name.window.slide.days")
	int windowSlideInDays = 1;
	@VisibleForTesting
	@Configuration("file.filter.name.window.date.end")
	String windowDateEndStr = LocalDate.now().toString();

	@Override
	public String filterSource(String source) {

		if (!source.contains(DATE_TOKEN)) {
			LOGGER.warn("Pattern {} not found, no filters applicable.", DATE_TOKEN);
			return source;
		}

		return getListOfDays()
				.stream()
				.map(date -> source.replace(DATE_TOKEN, date.toString()))
				.collect(Collectors.joining(","));
	}

	private Set<LocalDate> getListOfDays() {
		Set<LocalDate> applicableDays = new LinkedHashSet<>();
		LocalDate windowDateEnd = LocalDate.parse(windowDateEndStr).minusDays(windowSlideInDays);
		LocalDate windowBeginDate = windowDateEnd.minusDays(windowSizeInDays);
		long diffsDays = ChronoUnit.DAYS.between(windowBeginDate, windowDateEnd);

		for(int i = NumberUtils.INTEGER_ONE; i <= diffsDays; i++) {
			applicableDays.add(windowBeginDate.plusDays(i));
		}

		return applicableDays;
	}
}
