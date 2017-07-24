package com.elo7.nightfall.di.providers.reporter.statsd;

import com.netflix.governator.annotations.Configuration;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

class StatsDConfiguration implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank
	@Configuration("nightfall.statsd.prefix")
	private String prefix;
	@NotBlank
	@Configuration("nightfall.statsd.host")
	private String host;
	@Configuration("nightfall.statsd.port")
	private int port = 8125;
	@Configuration("nightfall.statsd.bufferSize")
	private int bufferSize = 512;

	public String getPrefix() {
		if (!prefix.endsWith(".")) {
			prefix = prefix + ".";
		}

		return prefix;

	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public int getBufferSize() {
		return bufferSize;
	}
}
