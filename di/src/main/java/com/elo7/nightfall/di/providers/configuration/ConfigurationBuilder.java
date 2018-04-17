package com.elo7.nightfall.di.providers.configuration;

import com.elo7.nightfall.di.providers.configuration.zookeeper.ZookeeperConfigurationLoader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class ConfigurationBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationBuilder.class);
	private final ConfigurationSchema schema;
	private List<String> additionalOptions = Collections.emptyList();

	public ConfigurationBuilder(ConfigurationSchema schema) {
		this.schema = schema;
	}

	public ConfigurationBuilder withAdditionalOptions(List<String> options) {
		if (options != null) {
			additionalOptions = options;
		}

		return this;
	}

	public Properties build() {
		Properties properties = loadProperties();

		additionalOptions.stream()
				.filter(StringUtils::isNotBlank)
				.filter(option -> option.contains("="))
				.forEach(option -> {
					String[] values = option.split("=");
					properties.put(values[0], values[1]);
				});

		return properties;
	}

	private Properties loadProperties() {
		try {
			Properties config = new Properties();
			InputStream properties;

			switch (schema.getSource()) {
				case "zookeeper":
					LOGGER.info("Loading configuration from Zookeeper: {}", schema);
					properties = ZookeeperConfigurationLoader.loadConfiguration(schema);
					break;
				case "file":
					properties = new FileInputStream(new File(schema.getPath()));
					break;
				case "classpath":
					properties = loadFromClasspath(schema.getPath());
					break;
				default:
					throw new IllegalArgumentException("Invalid configuration source {"
							+ schema.getSource()
							+ "} for Nightfall configuration");
			}

			if (properties == null) {
				throw new IllegalArgumentException("Invalid source schema [" + schema.getSource()
						+ "] or missing Nightfall configuration");
			}

			config.load(properties);
			return config;
		} catch (IOException e) {
			throw new RuntimeException("Failed to load Nightfall configuration!", e);
		}
	}

	private static InputStream loadFromClasspath(String path) {
		InputStream properties = ConfigurationBuilder.class.getResourceAsStream(path);

		if (properties == null) {
			properties = ClassLoader.getSystemResourceAsStream(path);
		}

		return properties;
	}
}
