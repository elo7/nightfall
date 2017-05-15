package com.elo7.nightfall.di.providers.configuration;

import com.elo7.nightfall.di.commons.configuration.option.PropertyOption;
import com.elo7.nightfall.di.commons.configuration.option.StringOption;

import java.util.ArrayList;
import java.util.List;

public enum DefaultPropertySource {
	SPARTA_CONFIGURATION(new StringOption(
			"e",
			"Nightfall configuration path, format: 'zookeeper://host:port,host:port/file/path', "
					+ "or 'file://file/path'. Defaults to classpath://nightfall.properties",
			false,
			"classpath://nightfall.properties"));

	private final PropertyOption<?> propertyOption;

	DefaultPropertySource(PropertyOption<?> propertyOption) {
		this.propertyOption = propertyOption;
	}

	public PropertyOption<?> getPropertyOption() {
		return propertyOption;
	}

	public static List<PropertyOption<?>> getPropertyOptions() {
		List<PropertyOption<?>> options = new ArrayList<>();

		for (DefaultPropertySource property : DefaultPropertySource.values()) {
			options.add(property.getPropertyOption());
		}

		return options;
	}
}
