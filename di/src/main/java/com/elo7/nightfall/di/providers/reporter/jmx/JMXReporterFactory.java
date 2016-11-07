package com.elo7.nightfall.di.providers.reporter.jmx;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.elo7.nightfall.di.Component;
import com.elo7.nightfall.di.providers.reporter.ApplicationType;
import com.elo7.nightfall.di.providers.reporter.MetricRegistryFactory;
import com.elo7.nightfall.di.providers.reporter.ReporterFactory;
import com.google.common.annotations.VisibleForTesting;

@Component
public class JMXReporterFactory implements ReporterFactory {

    private static final long serialVersionUID = 1L;
    private static final String DOT = ".";
    public static final String SPARK = "spark";
    private static transient JmxReporter jmxReporter;

    @Override
    public void send(String application, ApplicationType applicationType) {
        String metricName = getMetricName(application, applicationType);
        getOrCreate(metricName).start();
    }

    private JmxReporter getOrCreate(String metricName) {
        if (jmxReporter == null) {
            synchronized (this) {
                if (jmxReporter == null) {
                    MetricRegistry metricRegistry = MetricRegistryFactory.createFor(metricName);
                    jmxReporter = JmxReporter
                            .forRegistry(metricRegistry)
                            .inDomain(SPARK)
                            .build();
                }
            }
        }

        return jmxReporter;
    }

    @VisibleForTesting
    String getMetricName(String application, ApplicationType applicationType) {
        return applicationType.name().toLowerCase() + DOT + application;
    }
}
