package com.elo7.nightfall.persistence.cassandra.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * Time convertion functions for TimeUUID support due lack of it in Cassandra.
 * <p>
 * Magic number obtained from #cassandra's thobbs, who claims to have stolen it from a Python library.
 * </p>
 * See <a href='https://wiki.apache.org/cassandra/FAQ#working_with_timeuuid_in_java'>Working with timeUUID in Java</a>.
 */
public class TimeUUID {

    private static final long NUM_100NS_INTERVALS_SINCE_UUID_EPOCH = 0x01b21dd213814000L;

    public static UUID uuidForDate(Date date) {
        /*
          Magic number obtained from #cassandra's thobbs, who
          claims to have stolen it from a Python library.
          see: https://wiki.apache.org/cassandra/FAQ#working_with_timeuuid_in_java
        */
        long origTime = date.getTime();
        long time = origTime * 10000 + NUM_100NS_INTERVALS_SINCE_UUID_EPOCH;
        long timeLow = time & 0xffffffffL;
        long timeMid = time & 0xffff00000000L;
        long timeHi = time & 0xfff000000000000L;
        long upperLong = (timeLow << 32) | (timeMid >> 16) | (1 << 12) | (timeHi >> 48);
        return new UUID(upperLong, 0xC000000000000000L);
    }

    public static UUID uuidForZonedDateTime(ZonedDateTime zonedDateTime) {
        return uuidForLocalDateTime(zonedDateTime.toLocalDateTime());
    }

    public static UUID uuidForLocalDateTime(LocalDateTime dateTime) {
        Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        return uuidForDate(date);
    }

    public static UUID uuidForLocalDate(LocalDate localDate) {
        return uuidForLocalDateTime(localDate.atStartOfDay());
    }
}
