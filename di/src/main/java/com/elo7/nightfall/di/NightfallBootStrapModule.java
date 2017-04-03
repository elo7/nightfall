package com.elo7.nightfall.di;

import com.elo7.nightfall.di.providers.configuration.ConfigurationProvider;
import com.elo7.nightfall.di.providers.configuration.DefaultConfiguration;
import com.google.inject.Provider;
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
	private final Class<? extends Provider<?>> provider;

	NightfallBootStrapModule(String[] args, Class<? extends Provider<?>> provider) {
		this.args = args;
		this.provider = provider;
		configuration = new DefaultConfiguration(args);
	}

	@Override
	public void configure(BootstrapBinder binder) {
		Properties loadProperties = ConfigurationProvider.loadProperties(configuration.getConfigurationSchema());
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
		// Workaround to bind provider within ModuleProvider
		binder
				.bind(String.class)
				.annotatedWith(Names.named("contextProvider"))
				.toInstance(provider.getName());
	}
}
