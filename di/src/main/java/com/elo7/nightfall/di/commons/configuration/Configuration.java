package com.elo7.nightfall.di.commons.configuration;

import com.elo7.nightfall.di.commons.configuration.option.PropertyOption;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Configuration implements Serializable {

	private static final long serialVersionUID = 1L;
	private final CommandLine cmd;

	public Configuration(String[] args, List<PropertyOption<?>> propertyOptions) {
		Options options = new Options();
		for (PropertyOption<?> property : propertyOptions) {
			options.addOption(property.propertyOption());
		}

		CommandLineParser parser = new BasicParser();

		try {
			cmd = parser.parse(options, args, false);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T getConfiguration(PropertyOption<T> property) {
		Optional<T> defaultValue = property.defaultValue();
		String propertyValue = cmd.getOptionValue(property.propertyName());

		if (defaultValue.isPresent()) {

			if (propertyValue == null) {
				return defaultValue.get();
			} else {
				return property.parse(propertyValue);
			}
		} else if (cmd.hasOption(property.propertyName())) {
			return property.parse(propertyValue);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> Optional<T> getOptionalConfiguration(PropertyOption<?> propertyOption) {
		return Optional.ofNullable((T) getConfiguration(propertyOption));
	}

	public <T> List<T> getConfigurations(PropertyOption<T> property) {
		String[] optionValues = cmd.getOptionValues(property.propertyName());

		if (optionValues == null || optionValues.length == 0) {
			return Collections.emptyList();
		}

		return Stream.of(optionValues)
				.map(property::parse)
				.collect(Collectors.toList());
	}
}
