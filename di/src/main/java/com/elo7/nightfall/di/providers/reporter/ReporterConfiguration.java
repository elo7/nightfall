package com.elo7.nightfall.di.providers.reporter;

import com.netflix.governator.annotations.Configuration;

import java.io.Serializable;

public class ReporterConfiguration implements Serializable {

	private static final long serialVersionUID = 1L;

	@Configuration("reporter.enabled")
	private boolean reporterEnabled = true;

	public boolean isReporterEnabled() {
		return reporterEnabled;
	}
}
