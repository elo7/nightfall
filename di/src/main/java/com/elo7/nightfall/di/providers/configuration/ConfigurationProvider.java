package com.elo7.nightfall.di.providers.configuration;

import com.elo7.nightfall.di.providers.configuration.zookeeper.ZookeeperConfigurationLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationProvider.class);

	public static Properties loadProperties(ConfigurationSchema schema) {
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
					properties = ClassLoader.getSystemResourceAsStream(schema.getPath());
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

}
