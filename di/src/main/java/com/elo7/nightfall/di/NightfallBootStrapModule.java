package com.elo7.nightfall.di;

import com.elo7.nightfall.di.providers.configuration.ConfigurationBuilder;
import com.elo7.nightfall.di.providers.configuration.DefaultConfiguration;
import com.google.inject.name.Names;
import com.netflix.governator.configuration.CompositeConfigurationProvider;
import com.netflix.governator.configuration.PropertiesConfigurationProvider;
import com.netflix.governator.configuration.SystemConfigurationProvider;
import com.netflix.governator.guice.BootstrapBinder;
import com.netflix.governator.guice.BootstrapModule;

import java.util.Properties;

class NightfallBootStrapModule implements BootstrapModule {

	private final DefaultConfiguration configuration;
	private final String[] args;
	private final ExecutionMode mode;

	NightfallBootStrapModule(String[] args, ExecutionMode mode) {
		this.args = args;
		this.configuration = new DefaultConfiguration(args);
		this.mode = mode;
	}

	@Override
	public void configure(BootstrapBinder binder) {
		Properties loadProperties = new ConfigurationBuilder(configuration.getConfigurationSchema())
				.withAdditionalOptions(configuration.getAdditionalConfigurations())
				.build();
		CompositeConfigurationProvider configurationProvider = new CompositeConfigurationProvider(
				new SystemConfigurationProvider(),
				new PropertiesConfigurationProvider(loadProperties));
		NightfallConfigurations configurations = new NightfallConfigurations(loadProperties);

		binder
				.bind(NightfallConfigurations.class)
				.toInstance(configurations);
		binder
				.bindConfigurationProvider()
				.toInstance(configurationProvider);
		binder
				.bind(String[].class)
				.annotatedWith(Names.named("args"))
				.toInstance(args);
		binder
				.bind(ExecutionMode.class)
				.toInstance(mode);
	}
}