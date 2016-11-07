package com.elo7.nightfall.persistence.cassandra.util;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class TimeUUIDTest {
    private Date date;
    private UUID uuidForDate;

    @Before
    public void setup() {
        date = DateUtils.truncate(new Date(), Calendar.DATE);
        uuidForDate = TimeUUID.uuidForDate(date);
    }

    @Test
    public void shouldGenerateSameUUIDForZonedDateTime() {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        assertEquals(uuidForDate, TimeUUID.uuidForZonedDateTime(zonedDateTime));
    }

    @Test
    public void shouldGenerateSameUUIDForLocalDateTime() {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        assertEquals(uuidForDate, TimeUUID.uuidForLocalDateTime(localDateTime));
    }

    @Test
    public void shouldGenerateSameUUIDForLocalDate() {
        LocalDate localDate = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalDate();
        assertEquals(uuidForDate, TimeUUID.uuidForLocalDate(localDate));
    }
}
