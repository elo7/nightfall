package com.elo7.nightfall.di.providers.reporter.statsd;

import com.codahale.metrics.MetricRegistry;
import com.elo7.nightfall.di.Component;
import com.elo7.nightfall.di.providers.reporter.ApplicationType;
import com.elo7.nightfall.di.providers.reporter.MetricRegistryFactory;
import com.elo7.nightfall.di.providers.reporter.ReporterFactory;
import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;
import com.readytalk.metrics.StatsDReporter;

@Component
public class StatsDReporterFactory implements ReporterFactory {
    private static final long serialVersionUID = 1L;
    private static final String DOT = ".";
    private final StatsDConfiguration statsDConfiguration;
    private static transient StatsDReporter statsDReporter;

    @Inject
    StatsDReporterFactory(StatsDConfiguration statsDConfiguration) {
        this.statsDConfiguration = statsDConfiguration;
    }

    @Override
    public void send(String application, ApplicationType applicationType) {
        String metricName = getMetricName(application, applicationType);
        getOrCreate(metricName).report();
    }

    private StatsDReporter getOrCreate(String metricName) {
        if (statsDReporter == null) {
            synchronized (this) {
                if (statsDReporter == null) {
                    MetricRegistry metricRegistry = MetricRegistryFactory.createFor(metricName);
                    statsDReporter = StatsDReporter
                            .forRegistry(metricRegistry)
                            .build(statsDConfiguration.getHost(), statsDConfiguration.getPort());
                }
            }
        }

        return statsDReporter;
    }

    @VisibleForTesting
    String getMetricName(String application, ApplicationType applicationType) {
        String prefix = statsDConfiguration.getPrefix();

        if (!prefix.endsWith(DOT)) {
            prefix = prefix + DOT;
        }

        return prefix + applicationType.name().toLowerCase() + DOT + application;
    }
}
