package com.elo7.nightfall.di.providers.kafka.cassandra;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class CassandraConfigurationTest {

	private CassandraConfiguration subject;

	private Map<String, String> configuration;

	@Before
	public void setup() {
		configuration = new HashMap<>();

		configuration.put("cassandra.hosts", "host1,host2");
		configuration.put("cassandra.keyspace", "keyspace");
	}

	@Test
	public void shouldReturnEmptyWhenDataCenterNameIsNull() {
		subject = new CassandraConfiguration(null, null, 0, null, null, null, 0);

		assertEquals(subject.getDataCenterName(), Optional.empty());
	}

	@Test
	public void shouldReturnEmptyWhenDataCenterNameIsBlank() {
		subject = new CassandraConfiguration(null, null, 0, null, null, " ", 0);

		assertEquals(subject.getDataCenterName(), Optional.empty());
	}

	@Test
	public void shouldReturnPresentWhenDataCenterNameNotIsBlank() {
		subject = new CassandraConfiguration(null, null, 0, null, null, "dc", 0);

		assertEquals(subject.getDataCenterName(), Optional.of("dc"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionWhenHostIsBlank() {
		configuration.put("cassandra.keyspace", " ");

		CassandraConfiguration.build(configuration);

		fail("Blank Host is not allowed");
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionWhenKeySpaceIsBlank() {
		configuration.put("cassandra.hosts", " ");

		CassandraConfiguration.build(configuration);

		fail("Blank keyspace is not allowed");
	}

	@Test
	public void shouldBuildConfigurationWhenHostsAndKeySpaceAreNotBlank() {
		CassandraConfiguration result = CassandraConfiguration.build(configuration);

		assertThat(result.getConnectionPoints(), arrayContaining("host1", "host2"));
		assertEquals("keyspace", result.getKeySpace());
	}

	@Test
	public void shouldUseDefaultPortWhenIsMissing() {
		CassandraConfiguration result = CassandraConfiguration.build(configuration);

		assertEquals(9042, result.getPort());
	}

	@Test
	public void shouldUsePortConfigurationWhenIsNotMissing() {
		configuration.put("cassandra.port", "22");

		CassandraConfiguration result = CassandraConfiguration.build(configuration);

		assertEquals(22, result.getPort());
	}

	@Test
	public void shouldUseDefaultTTLWhenIsMissing() {
		CassandraConfiguration result = CassandraConfiguration.build(configuration);

		assertEquals(7 * 60 * 60 * 24, result.getOffsetRangeHistoryTTLInSeconds());
	}

	@Test
	public void shouldUseTTLConfigurationWhenIsNotMissing() {
		configuration.put("offsetRange.history.ttl.days", "2");

		CassandraConfiguration result = CassandraConfiguration.build(configuration);

		assertEquals(2 * 60 * 60 * 24, result.getOffsetRangeHistoryTTLInSeconds());
	}
}