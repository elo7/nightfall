package com.elo7.nightfall.di.providers.reporter.statsd;

import com.netflix.governator.annotations.Configuration;

import java.io.Serializable;


public class StatsDConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Configuration("reporter.statsd.host")
    private String host = "localhost";

    @Configuration("reporter.statsd.port")
    private int port = 8125;

    @Configuration("reporter.statsd.prefix")
    private String prefix = "spark";

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPrefix() {
        return prefix;
    }
}
