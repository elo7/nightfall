package com.elo7.nightfall.di.providers.reporter;

import com.elo7.nightfall.di.providers.reporter.statsd.StatsDReporterFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReporterFactoryProviderTest {
    private ReporterFactoryProvider subject;
    private Set<ReporterFactory> factories;
    @Mock
    private ReporterConfiguration reporterConfiguration;
    @Mock
    private StatsDReporterFactory statsDReporterFactory;


    @Before
    public void init() {
        factories = new HashSet<>();
        factories.add(statsDReporterFactory);
        subject = new ReporterFactoryProvider(factories, reporterConfiguration);
    }

    @Test
    public void shouldReturnReporterFactoryImplementationWhenReporterClassWasFound() {
        when(reporterConfiguration.getReporterClass()).thenReturn(statsDReporterFactory.getClass().getName());

        assertTrue(subject.get() instanceof ReporterFactory);
    }

    @Test(expected = UnknownReporterFactoryException.class)
    public void shouldThrowExceptionWhenUnknownReporterFactoryImplementation() {
        when(reporterConfiguration.getReporterClass()).thenReturn(this.getClass().getName());
        subject.get();
        fail();
    }
}
