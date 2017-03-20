package com.elo7.nightfall.di.providers.reporter;

import com.google.inject.Provider;
import com.google.inject.Provides;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.Set;

public class ReporterFactoryProvider implements Provider<ReporterFactory> {

    private final Set<ReporterFactory> factories;
    private final ReporterConfiguration reporterConfiguration;

    @Inject
    public ReporterFactoryProvider(Set<ReporterFactory> factories, ReporterConfiguration reporterConfiguration) {
        this.factories = factories;
        this.reporterConfiguration = reporterConfiguration;
    }

    @Override
    @Provides
    @Singleton
    public ReporterFactory get() {
        Optional<ReporterFactory> foundRepository = factories
                .stream()
                .filter(candidate -> candidate.getClass().getName().equals(reporterConfiguration.getReporterClass()))
                .findFirst();

        if (foundRepository.isPresent()) {
            return foundRepository.get();
        }

        throw new UnknownReporterFactoryException("Unknown ReporterFactory Implementation: " + reporterConfiguration.getReporterClass());
    }
}
