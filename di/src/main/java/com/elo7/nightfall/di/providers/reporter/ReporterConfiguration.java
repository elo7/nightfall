package com.elo7.nightfall.di.providers.reporter;

import com.netflix.governator.annotations.Configuration;

import java.io.Serializable;

public class ReporterConfiguration implements Serializable {

	private static final long serialVersionUID = 1L;

	@Configuration("reporter.enabled")
	private boolean reporterEnabled = true;
	@Configuration("reporter.class")
	private String reporterClass = ""; //ConsoleReporterFactory.class.getName();

	public boolean isReporterEnabled() {
		return reporterEnabled;
	}
}
