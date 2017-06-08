package com.elo7.nightfall.di.providers.reporter;

import com.elo7.nightfall.di.providers.reporter.console.ConsoleReporter;
import com.netflix.governator.annotations.Configuration;

import java.io.Serializable;

public class ReporterConfiguration implements Serializable {

	private static final long serialVersionUID = 1L;

	@Configuration("nightfall.reporter.enabled")
	private boolean reporterEnabled = true;
	@Configuration("nightfall.reporter.class")
	private String reporterClass = ConsoleReporter.class.getName();

	public boolean isReporterEnabled() {
		return reporterEnabled;
	}

	public String getReporterClass() {
		return reporterClass;
	}
}
