package com.elo7.nightfall.di;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
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
		String property = properties.getProperty(key);

		if (StringUtils.isBlank(property)) {
			return Optional.empty();
		}

		return Optional.of(property);
	}

	public Map<String, String> getPropertiesWithPrefix(String keyPrefix) {
		if (StringUtils.isBlank(keyPrefix)) {
			return Collections.emptyMap();
		}

		return properties.entrySet().stream()
				.filter(entry -> ((String) entry.getKey()).startsWith(keyPrefix))
				.collect(Collectors.toMap(
						entry -> entry.getKey().toString(),
						entry -> Strings.emptyToNull(entry.getValue().toString())));
	}

	public Map<String, String> getAllProperties() {
		return properties.entrySet().stream()
				.collect(Collectors.toMap(
						entry -> entry.getKey().toString(),
						entry -> Strings.emptyToNull(entry.getValue().toString())));
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("properties", properties)
				.toString();
	}
}
