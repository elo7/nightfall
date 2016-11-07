package com.elo7.nightfall.persistence.cassandra;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DefaultCassandraConfigurationTest {

    private DefaultCassandraConfiguration subject;

    @Before
    public void setup() {
        subject = new DefaultCassandraConfiguration();
    }

    @Test
    public void shouldReturnEmptyWhenDataCenterNameIsNull() {
        subject.setDataCenterName(null);

        Optional<String> optionalDC = subject.getDataCenterName();

        assertFalse(optionalDC.isPresent());
    }

    @Test
    public void shouldReturnEmptyWhenDataCenterNameIsBlank() {
        subject.setDataCenterName(" ");

        Optional<String> optionalDC = subject.getDataCenterName();

        assertFalse(optionalDC.isPresent());
    }

    @Test
    public void shouldReturnPresentWhenDataCenterNameIsValid() {
        subject.setDataCenterName("dataCenter");

        Optional<String> optionalDC = subject.getDataCenterName();

        assertTrue(optionalDC.isPresent());
        assertEquals("dataCenter", optionalDC.get());
    }
}
