package com.elo7.nightfall.di.providers.reporter.console;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.elo7.nightfall.di.Component;
import com.elo7.nightfall.di.providers.reporter.ApplicationType;
import com.elo7.nightfall.di.providers.reporter.MetricRegistryFactory;
import com.elo7.nightfall.di.providers.reporter.ReporterFactory;

@Component
public class ConsoleReporterFactory implements ReporterFactory {
    private static final long serialVersionUID = 1L;
    public static final String DOT = ".";
    private static transient ConsoleReporter consoleReporter;

    @Override
    public void send(String application, ApplicationType applicationType) {
        getOrCreate(application, applicationType).report();
    }

    private ConsoleReporter getOrCreate(String application, ApplicationType applicationType) {
        if (consoleReporter == null) {
            synchronized (this) {
                if (consoleReporter == null) {
                    MetricRegistry metricRegistry = MetricRegistryFactory.createFor(applicationType + DOT + application);
                    consoleReporter = ConsoleReporter
                            .forRegistry(metricRegistry)
                            .build();
                }
            }
        }

        return consoleReporter;
    }
}
