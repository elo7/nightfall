package com.elo7.nightfall.di.providers.reporter.statsd;

import com.google.inject.Inject;
import com.google.inject.Provider;

import java.io.IOException;

class StatsDProvider implements Provider<StatsD> {

	private final StatsDConfiguration configuration;

	@Inject
	StatsDProvider(final StatsDConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public StatsD get() {
		try {
			return new RemoteStatsD(new UDPSender(
					configuration.getHost(),
					configuration.getPort()),
					configuration.getBufferSize(),
					configuration.getPrefix());
		} catch (IOException e) {
			throw new RuntimeException("Failed to create StatsD client", e);
		}
	}
}
