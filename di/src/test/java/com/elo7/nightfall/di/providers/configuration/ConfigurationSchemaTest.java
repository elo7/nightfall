package com.elo7.nightfall.di.providers.configuration;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ConfigurationSchemaTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenConfigurationStringIsEmpty() {
        ConfigurationSchema.create(" ");
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenConfigurationStringNull() {
        ConfigurationSchema.create(null);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenConfigurationStringHasNoSeparator() {
        ConfigurationSchema.create("schemaWithoutSeparator");
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenConfigurationStringPathIsEmpty() {
        ConfigurationSchema.create("emptyPath:// ");
        fail();
    }

    @Test
    public void shouldCreateConfigurationSchema() {
        ConfigurationSchema schema = ConfigurationSchema.create("source://path/sub.other");

        assertEquals("source", schema.getSource());
        assertEquals("path/sub.other", schema.getPath());
    }


}
