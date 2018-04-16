package com.elo7.nightfall.di.commons.configuration;

import com.elo7.nightfall.di.commons.configuration.option.PropertyOption;
import com.elo7.nightfall.di.commons.configuration.option.StringOption;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class ConfigurationTest {

	private final String PROPERTY_NAME = "property";
	private final String PROPERTY_VALUE = "value";
	private final String PROPERTY_DESCRIPTION = "description";
	private final String PROPERTY_COMMAND_LINE = "-" + PROPERTY_NAME;

	@Test
	public void shouldReturnPropertyValueWhenPropertyValueIsDefinedAndDefaultValueIsAbsent() {
		String[] args = {PROPERTY_COMMAND_LINE, PROPERTY_VALUE};
		PropertyOption<?> property = buildPropertyOption(null);
		List<PropertyOption<?>> properties = Collections.singletonList(property);
		Configuration configuration = new Configuration(args, properties);

		assertEquals(PROPERTY_VALUE, configuration.getConfiguration(property));
	}

	@Test
	@Ignore
	public void shouldReturnNullWhenPropertyValueNotDefined() {
		String[] args = {PROPERTY_COMMAND_LINE, PROPERTY_VALUE};
		PropertyOption<?> property = buildPropertyOption(null);
		List<PropertyOption<?>> properties = Collections.emptyList();
		Configuration configuration = new Configuration(args, properties);

		assertNull(configuration.getConfiguration(property));
	}

	@Test
	public void shouldReturnPropertyDefaultValueWhenPropertyValueNotDefinedAndDefaultValueIsPresent() {
		String[] args = {};
		PropertyOption<?> property = buildPropertyOption(PROPERTY_VALUE);
		List<PropertyOption<?>> properties = Collections.singletonList(property);
		Configuration configuration = new Configuration(args, properties);

		assertEquals(PROPERTY_VALUE, configuration.getConfiguration(property));
	}

	@Test
	public void shouldReturnPropertyValueWhenPropertyIsDefinedAndDefaultValueIsPresent() {
		String[] args = {PROPERTY_COMMAND_LINE, PROPERTY_VALUE};
		PropertyOption<?> property = buildPropertyOption("defaultValue");
		List<PropertyOption<?>> properties = Collections.singletonList(property);
		Configuration configuration = new Configuration(args, properties);

		assertEquals(PROPERTY_VALUE, configuration.getConfiguration(property));
	}

	@Test
	public void shouldReturnEmptyListForMultipleValuesPropertiesWhenValueIsAbsent() {
		String[] args = {};
		PropertyOption<String> property = new StringOption("p", PROPERTY_DESCRIPTION, false, null);
		List<PropertyOption<?>> properties = Collections.singletonList(property);
		Configuration configuration = new Configuration(args, properties);

		List<String> result = configuration.getConfigurations(property);
		assertThat(result, is(Matchers.empty()));
	}

	@Test
	public void shouldReturnListForMultipleValuesPropertiesWhenValuesIsPresent() {
		String[] args = {"-p", "one", "-p", "two"};
		PropertyOption<String> property = new StringOption("p", PROPERTY_DESCRIPTION, false, null);
		List<PropertyOption<?>> properties = Collections.singletonList(property);
		Configuration configuration = new Configuration(args, properties);

		List<String> result = configuration.getConfigurations(property);
		assertThat(result, hasItems("one", "two"));
	}

	@Test
	public void shouldReturnFirstValueForMultipleValuesPropertiesWhenValuesIsPresent() {
		String[] args = {"-p", "one", "-p", "two"};
		PropertyOption<String> property = new StringOption("p", PROPERTY_DESCRIPTION, false, null);
		List<PropertyOption<?>> properties = Collections.singletonList(property);
		Configuration configuration = new Configuration(args, properties);

		assertEquals("one", configuration.getConfiguration(property));
	}

	private PropertyOption<?> buildPropertyOption(String defaultValue) {
		return new StringOption(PROPERTY_NAME, PROPERTY_DESCRIPTION, false, defaultValue);
	}
}
