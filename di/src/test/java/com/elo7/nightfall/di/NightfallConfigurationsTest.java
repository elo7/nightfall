package com.elo7.nightfall.di;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NightfallConfigurationsTest {

	@InjectMocks
	private NightfallConfigurations subject;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private Properties properties;

	@Test
	public void shouldReturnEmptyWhenPropertyIsMissing() {
		Optional<String> result = subject.getProperty("missing.property");

		assertFalse(result.isPresent());
	}

	@Test
	public void shouldReturnEmptyWhenPropertyValueIsBlank() {
		when(properties.getProperty("blank.property")).thenReturn(" ");

		Optional<String> result = subject.getProperty("blank.property");

		assertFalse(result.isPresent());
	}

	@Test
	public void shouldReturnPropertyWhenPropertyValueIsValid() {
		when(properties.getProperty("valid.property")).thenReturn("value");

		Optional<String> result = subject.getProperty("valid.property");

		assertTrue(result.isPresent());
		assertEquals("value", result.get());
	}

	@Test
	public void shouldReturnEmptyWhenPrefixIsNull() {
		Map<String, String> result = subject.getPropertiesWithPrefix(null);

		assertTrue(result.isEmpty());
	}

	@Test
	public void shouldReturnEmptyWhenPrefixIsBlank() {
		Map<String, String> result = subject.getPropertiesWithPrefix(" ");

		assertTrue(result.isEmpty());
	}

	@Test
	public void shouldExcludePropertiesWithNullValuesForPrefixFiltering() {
		Set<Map.Entry<Object, Object>> entries = new EntrySetBuilder()
				.with("key1.prefix1", "value1")
				.with("key1.null.value", null)
				.with("key2.prefix", "value3")
				.build();
		when(properties.entrySet()).thenReturn(entries);

		Map<String, String> result = subject.getPropertiesWithPrefix("key1.");

		assertFalse(result.containsKey("key1.null.value"));
		assertTrue(result.containsKey("key1.prefix1"));
	}

	@Test
	public void shouldExcludePropertiesWithBlankValuesForPrefixFiltering() {
		Set<Map.Entry<Object, Object>> entries = new EntrySetBuilder()
				.with("key1.prefix1", "value1")
				.with("key1.blank.value", " ")
				.with("key2.prefix", "value3")
				.build();
		when(properties.entrySet()).thenReturn(entries);

		Map<String, String> result = subject.getPropertiesWithPrefix("key1.");

		assertFalse(result.containsKey("key1.blank"));
		assertTrue(result.containsKey("key1.prefix1"));
	}

	@Test
	public void shouldReturnEmptyWhenNoPropertiesMatchesThePrefix() {
		Set<Map.Entry<Object, Object>> entries = new EntrySetBuilder()
				.with("key1.prefix1", "value1")
				.with("key1.prefix2", "value2")
				.with("key2.prefix", "value3")
				.build();
		when(properties.entrySet()).thenReturn(entries);

		Map<String, String> result = subject.getPropertiesWithPrefix("unknown.");

		assertTrue(result.isEmpty());
	}

	@Test
	public void shouldReturnOnlyPropertiesThatMatchesThePrefix() {
		Set<Map.Entry<Object, Object>> entries = new EntrySetBuilder()
				.with("key1.prefix1", "value1")
				.with("key1.prefix2", "value2")
				.with("key2.prefix", "value3")
				.build();
		when(properties.entrySet()).thenReturn(entries);

		Map<String, String> result = subject.getPropertiesWithPrefix("key1.");

		assertFalse(result.isEmpty());
		assertTrue(result.containsKey("key1.prefix1"));
		assertTrue(result.containsKey("key1.prefix2"));
		assertFalse(result.containsKey("key2.prefix"));
	}

	@Test
	public void shouldReturnAllProperties() {
		Set<Map.Entry<Object, Object>> entries = new EntrySetBuilder()
				.with("key1", "value1")
				.with("key2", "value2")
				.with("key3", "value3")
				.build();
		when(properties.entrySet()).thenReturn(entries);

		Map<String, String> result = subject.getAllProperties();

		assertFalse(result.isEmpty());
		assertTrue(result.containsKey("key1"));
		assertTrue(result.containsKey("key2"));
		assertTrue(result.containsKey("key3"));
	}

	private static class EntrySetBuilder {
		private HashMap<Object, Object> map = new HashMap<>();

		EntrySetBuilder with(Object key, Object value) {
			map.put(key, value);
			return this;
		}

		Set<Map.Entry<Object, Object>> build() {
			return map.entrySet();
		}
	}

}