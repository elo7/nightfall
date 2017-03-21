package com.elo7.nightfall.di;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

public class NightfallConfigurations implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Properties properties;

	NightfallConfigurations(Properties properties) {
		this.properties = properties;
	}

	public Optional<String> getProperty(String key) {
		return Optional.ofNullable(properties.getProperty(key));
	}

	public Map<String, String> getPropertiesWithPrefix(String keyPrefix) {
		if (StringUtils.isBlank(keyPrefix)) {
			return Collections.emptyMap();
		}

		return properties.entrySet().stream()
				.filter(entry -> ((String) entry.getKey()).startsWith(keyPrefix))
				.collect(Collectors.toMap(entry -> entry.getKey().toString(), entry -> entry.getValue().toString()));
	}
}
