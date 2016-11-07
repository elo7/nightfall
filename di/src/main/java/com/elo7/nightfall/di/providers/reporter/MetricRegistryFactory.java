package com.elo7.nightfall.di.providers.reporter;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.Inject;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;


public class MetricRegistryFactory implements Serializable {
    private static final long serialVersionUID = 1L;

    public static MetricRegistry createFor(String metricName) {
        MetricRegistry registry = new MetricRegistry();

        Gauge<Integer> gauge = () -> NumberUtils.INTEGER_ONE;
        registry.register(metricName, gauge);
        return registry;
    }
}
