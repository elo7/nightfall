package com.elo7.nightfall.di.providers.reporter;

import com.elo7.nightfall.di.Component;
import com.google.inject.Inject;
import org.apache.spark.SparkContext;

import java.io.Serializable;

@Component
public class ReporterSender implements Serializable {
    private static final long serialVersionUID = 1L;
    private final ReporterConfiguration reporterConfiguration;
    private final ReporterFactory factory;

    @Inject
    ReporterSender(ReporterConfiguration reporterConfiguration, ReporterFactory factory) {
        this.reporterConfiguration = reporterConfiguration;
        this.factory = factory;
    }

    public void sendReport(SparkContext sparkContext, ApplicationType applicationType) {
        if (reporterConfiguration.isReporterEnabled()) {
            factory.send(sparkContext.appName(), applicationType);
        }
    }
}
