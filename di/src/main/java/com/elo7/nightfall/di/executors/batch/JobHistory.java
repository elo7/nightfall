package com.elo7.nightfall.di.executors.batch;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Properties;

import com.elo7.nightfall.di.NightfallConfigurations;
import com.elo7.nightfall.di.commons.util.DateConvertionUtils;
import com.google.common.base.MoreObjects;

public class JobHistory {

	private final String appName;
	private final NightfallConfigurations configurations;
	private final LocalDateTime startedAt;
	private LocalDateTime finishedAt;

	JobHistory(String appName, NightfallConfigurations configurations, LocalDateTime startedAt) {
		this.appName = appName;
		this.configurations = configurations;
		this.startedAt = startedAt;
	}

	public static JobHistory start(String appName, NightfallConfigurations configurations) {
		return new JobHistory(appName, configurations, LocalDateTime.now());
	}

	public JobHistory finish() {
		finishedAt = LocalDateTime.now();
		return this;
	}

	public Date getStartedAt() {
		return DateConvertionUtils.toDate(startedAt);
	}

	public Date getFinishedAt() {
		return DateConvertionUtils.toDate(finishedAt);
	}

	public String getAppName() {
		return appName;
	}

	public NightfallConfigurations getConfigurations() {
		return configurations;
	}

	public long getElapseTimeInSeconds() {
		return Duration.between(startedAt, finishedAt).getSeconds();
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("appName", appName)
				.add("startedAt", startedAt)
				.add("finishedAt", finishedAt)
				.add("configurations", configurations)
				.toString();
	}

}
