package com.elo7.nightfall.di.providers.reporter.statsd;

import com.elo7.nightfall.di.providers.reporter.ApplicationType;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StatsDReporterFactoryTest {
    private static final String TEST_SPARK_APPLICATION = StatsDReporterFactoryTest.class.getName();
    private static final String TEST_SPARK_PREFIX = "test.spark";
    private static final String TEST_SPARK_PREFIX_DOT = "test.spark.";
    private static final String DOT = ".";
    private String metricName;

    @InjectMocks
    private StatsDReporterFactory subject;
    @Mock
    private StatsDConfiguration statsDConfiguration;

    @Before
    public void init() {
        when(statsDConfiguration.getPrefix()).thenReturn(TEST_SPARK_PREFIX);
        metricName = subject.getMetricName(TEST_SPARK_APPLICATION, ApplicationType.BATCH);
    }

    @Test
    public void shouldGetMetricName() {
        String expected = TEST_SPARK_PREFIX + DOT + ApplicationType.BATCH.name().toLowerCase() + DOT + TEST_SPARK_APPLICATION;
        assertEquals(expected, metricName);
    }

    @Test
    public void shouldMetricNameContainsPrefix() {
        assertTrue(StringUtils.contains(metricName, TEST_SPARK_PREFIX));
    }

    @Test
    public void shouldConcatDotInTheEndOfPrefixWhenNotEndWithDot() {
        assertEquals(DOT, String.valueOf(metricName.charAt(TEST_SPARK_PREFIX.length())));
    }

    @Test
    public void shouldNotConcatDotInTheEndOfPrefixWhenNotEndWithDot() {
        when(statsDConfiguration.getPrefix()).thenReturn(TEST_SPARK_PREFIX_DOT);

        assertEquals(DOT, String.valueOf(metricName.charAt(TEST_SPARK_PREFIX_DOT.length() - 1)));
    }

    @Test
    public void shouldMetricNameContainsApplicationName() {
        assertTrue(StringUtils.contains(metricName, TEST_SPARK_APPLICATION));
    }

    @Test
    public void shouldMetricNameContainsApplicationType() {
        assertTrue(StringUtils.contains(metricName, ApplicationType.BATCH.name().toLowerCase()));
    }

    @Test
    public void shouldMetricStartsWithStatsDPrefix() {
        assertTrue(StringUtils.startsWith(metricName, TEST_SPARK_PREFIX));
    }

    @Test
    public void shouldMetricNameEndsWithApplicationName() {
        assertTrue(StringUtils.endsWith(metricName, TEST_SPARK_APPLICATION));
    }
}
