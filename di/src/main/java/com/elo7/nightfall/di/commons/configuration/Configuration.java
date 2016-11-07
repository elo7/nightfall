package com.elo7.nightfall.di.commons.configuration;


import com.elo7.nightfall.di.commons.configuration.option.PropertyOption;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Configuration implements Serializable {

    private static final long serialVersionUID = 1L;
    private final Map<PropertyOption<?>, Object> properties = new HashMap<>();

    public Configuration(String[] args, List<PropertyOption<?>> propertyOptions) {
        Options options = new Options();
        for (PropertyOption<?> property : propertyOptions) {
            options.addOption(property.propertyOption());
        }

        CommandLineParser parser = new BasicParser();

        try {
            CommandLine cmd = parser.parse(options, args, false);

            for (PropertyOption<?> property : propertyOptions) {
                populateProperties(cmd, property);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void populateProperties(CommandLine cmd, PropertyOption<?> property) {
        Optional<?> defaultValue = property.defaultValue();
        String propertyValue = cmd.getOptionValue(property.propertyName());

        if (defaultValue.isPresent()) {

            if (propertyValue == null) {
                properties.put(property, defaultValue.get());
            } else {
                properties.put(property, property.parse(propertyValue));
            }
        } else if (cmd.hasOption(property.propertyName())) {
            properties.put(property, property.parse(propertyValue));
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getConfiguration(PropertyOption<?> propertyOption) {
        return (T) properties.get(propertyOption);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getOptionalConfiguration(PropertyOption<?> propertyOption) {
        return Optional.ofNullable((T) properties.get(propertyOption));
    }
}
