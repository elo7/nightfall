package com.elo7.nightfall.di.commons.configuration;

import com.elo7.nightfall.di.commons.configuration.option.PropertyOption;
import com.elo7.nightfall.di.commons.configuration.option.StringOption;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConfigurationTest {

    private final String PROPERTY_NAME = "property";
    private final String PROPERTY_VALUE = "value";
    private final String PROPERTY_DESCRIPTION = "description";
    private final String PROPERTY_COMMAND_LINE = "-" + PROPERTY_NAME;

    @Test
    public void shouldReturnPropertyValueWhenPropertyValueIsDefinedAndDefaultValueIsAbsent() {
        String[] args = {PROPERTY_COMMAND_LINE, PROPERTY_VALUE};
        PropertyOption<?> property = buildPropertyOption(null);
        List<PropertyOption<?>> properties = Arrays.asList(property);
        Configuration configuration = new Configuration(args, properties);

        Assert.assertEquals(PROPERTY_VALUE, configuration.getConfiguration(property));
    }

    @Test @Ignore
    public void shouldReturnNullWhenPropertyValueNotDefined() {
        String[] args = {PROPERTY_COMMAND_LINE, PROPERTY_VALUE};
        PropertyOption<?> property = buildPropertyOption(null);
        List<PropertyOption<?>> properties = Collections.emptyList();
        Configuration configuration = new Configuration(args, properties);

        Assert.assertNull(configuration.getConfiguration(property));
    }

    @Test
    public void shouldReturnPropertyDefaultValueWhenPropertyValueNotDefinedAndDefaultValueIsPresent() {
        String[] args = {};
        PropertyOption<?> property = buildPropertyOption(PROPERTY_VALUE);
        List<PropertyOption<?>> properties = Arrays.asList(property);
        Configuration configuration = new Configuration(args, properties);

        Assert.assertEquals(PROPERTY_VALUE, configuration.getConfiguration(property));
    }

    @Test
    public void shouldReturnPropertyValueWhenPropertyIsDefinedAndDefaultValueIsPresent() {
        String[] args = {PROPERTY_COMMAND_LINE, PROPERTY_VALUE};
        PropertyOption<?> property = buildPropertyOption("defaultValue");
        List<PropertyOption<?>> properties = Arrays.asList(property);
        Configuration configuration = new Configuration(args, properties);

        Assert.assertEquals(PROPERTY_VALUE, configuration.getConfiguration(property));
    }

    private PropertyOption<?> buildPropertyOption(String defaultValue) {
        return new StringOption(PROPERTY_NAME, PROPERTY_DESCRIPTION, false, defaultValue);
    }
}
