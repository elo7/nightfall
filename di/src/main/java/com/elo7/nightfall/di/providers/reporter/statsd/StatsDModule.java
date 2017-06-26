package com.elo7.nightfall.di.providers.reporter.statsd;

import com.elo7.nightfall.di.ModuleProvider;
import com.elo7.nightfall.di.NightfallConfigurations;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import org.apache.commons.lang3.BooleanUtils;

@ModuleProvider
public class StatsDModule extends AbstractModule {

	private final NightfallConfigurations configuration;

	@Inject
	StatsDModule(final NightfallConfigurations configuration) {
		this.configuration = configuration;
	}

	@Override
	protected void configure() {
		Boolean isStatsDEnabled = configuration
				.getProperty("com.elo7.nightfall.di.providers.reporter.statsd.StatsDReporter")
				.map(BooleanUtils::toBoolean)
				.orElse(false);

		if (isStatsDEnabled) {
			bind(StatsD.class).toProvider(StatsDProvider.class);
		}
	}
}
