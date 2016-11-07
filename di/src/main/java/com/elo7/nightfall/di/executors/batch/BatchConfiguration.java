package com.elo7.nightfall.di.executors.batch;

import com.netflix.governator.annotations.Configuration;

import java.io.Serializable;

public class BatchConfiguration implements Serializable {

	private static final long serialVersionUID = 1L;

	@Configuration("batch.history.repository")
	private String jobHistoryRepositoryClass = ConsoleJobHistory.class.getName();

	public String getJobHistoryRepositoryClass() {
		return jobHistoryRepositoryClass;
	}
}
