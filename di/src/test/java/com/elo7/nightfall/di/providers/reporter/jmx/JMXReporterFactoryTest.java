package com.elo7.nightfall.di.providers.reporter.jmx;

import com.elo7.nightfall.di.providers.reporter.ApplicationType;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class JMXReporterFactoryTest {
    private static final String TEST_SPARK_APPLICATION = JMXReporterFactoryTest.class.getName();
    private static final String DOT = ".";
    private String metricName;

    @InjectMocks
    private JMXReporterFactory subject;

    @Before
    public void init() {
        metricName = subject.getMetricName(TEST_SPARK_APPLICATION, ApplicationType.STREAM);
    }

    @Test
    public void shouldGetMetricName() {
        String expected = ApplicationType.STREAM.name().toLowerCase() + DOT + TEST_SPARK_APPLICATION;
        assertEquals(expected, metricName);
    }

    @Test
    public void shouldMetricNameContainsApplicationType() {
        assertTrue(StringUtils.contains(metricName, ApplicationType.STREAM.name().toLowerCase()));
    }

    @Test
    public void shouldMetricStartsWithApplicationType() {
        assertTrue(StringUtils.startsWith(metricName, ApplicationType.STREAM.name().toLowerCase()));
    }

    @Test
    public void shouldMetricNameContainsApplicationName() {
        assertTrue(StringUtils.contains(metricName, TEST_SPARK_APPLICATION));
    }

    @Test
    public void shouldMetricNameEndsWithApplicationName() {
        assertTrue(StringUtils.endsWith(metricName, TEST_SPARK_APPLICATION));
    }

    @Test
    public void shouldMetricNameContainsDotAfterApplicationType() {
        assertEquals(DOT, String.valueOf(metricName.charAt(ApplicationType.STREAM.name().toLowerCase().length())));
    }
}
