package com.elo7.nightfall.di.providers.configuration;

import com.elo7.nightfall.di.commons.configuration.Configuration;

import java.util.List;

public class DefaultConfiguration {

	private final Configuration configuration;

	public DefaultConfiguration(String[] args) {
		configuration = new Configuration(args, DefaultPropertySource.getPropertyOptions());
	}

	public ConfigurationSchema getConfigurationSchema() {
		String configuration = (String) this.configuration.getConfiguration(DefaultPropertySource.SPARTA_CONFIGURATION.getPropertyOption());
		return ConfigurationSchema.create(configuration);
	}

	@SuppressWarnings("unchecked")
	public List<String> getAdditionalConfigurations() {
		return (List<String>) configuration.getConfigurations(DefaultPropertySource.CONF.getPropertyOption());
	}
}
