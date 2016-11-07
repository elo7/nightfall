package com.elo7.nightfall.distcp.configuration;

import com.google.common.annotations.VisibleForTesting;
import com.netflix.governator.annotations.Configuration;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.LinkedHashSet;
import java.util.Set;

public class NightfallDistcpConfiguration implements Serializable {

	private static final long serialVersionUID = 1L;

	@VisibleForTesting
	@Min(1)
	@Configuration("distcp.window.size.days")
	int windowSizeInDays = 1;

	@VisibleForTesting
	@Configuration("distcp.window.date.end")
	String windowDateEndStr = LocalDate.now().toString();

	@VisibleForTesting
	@Configuration("distcp.window.type")
	String type = DistcpType.DAY.name();

	@NotBlank
	@Configuration("distcp.output.dir")
	private String outputDir;

	public String getOutputDir() {
		return outputDir;
	}

	public Set<LocalDate> getWindowDays() {
		Set<LocalDate> applicableDays;
		switch (DistcpType.valueOf(type)) {
			case MONTH:
				applicableDays = generateDatesByMonth();
				break;
			case DAY:
			default:
				applicableDays = generateDatesBySize();
		}
		return applicableDays;
	}

	private Set<LocalDate> generateDatesBySize() {
		Set<LocalDate> applicableDays = new LinkedHashSet<>();
		LocalDate startDate = LocalDate.parse(windowDateEndStr).minusDays(windowSizeInDays);

		for (int days = NumberUtils.INTEGER_ONE; days <= windowSizeInDays; days++) {
			applicableDays.add(startDate.plusDays(days));
		}

		return applicableDays;
	}

	private Set<LocalDate> generateDatesByMonth() {
		Set<LocalDate> applicableDays = new LinkedHashSet<>();
		LocalDate start = LocalDate.parse(windowDateEndStr).with(TemporalAdjusters.firstDayOfMonth());
		LocalDate end = start.with(TemporalAdjusters.lastDayOfMonth());
		Period period = Period.between(start, end);

		for(int i=0; i <= period.getDays(); i++){
			applicableDays.add(start.plusDays(i));
		}

		return applicableDays;
	}

}
