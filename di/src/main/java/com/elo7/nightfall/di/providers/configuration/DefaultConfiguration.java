package com.elo7.nightfall.di.providers.configuration;

import com.elo7.nightfall.di.commons.configuration.Configuration;

public class DefaultConfiguration {

    private final Configuration configuration;

    public DefaultConfiguration(String[] args) {
        configuration = new Configuration(args, DefaultPropertySource.getPropertyOptions());
    }

    public ConfigurationSchema getConfigurationSchema() {
        return ConfigurationSchema.create(
                configuration.getConfiguration(DefaultPropertySource.SPARTA_CONFIGURATION.getPropertyOption()));
    }
}
