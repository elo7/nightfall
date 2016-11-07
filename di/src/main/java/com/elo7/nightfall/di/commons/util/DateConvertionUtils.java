package com.elo7.nightfall.di.commons.util;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

public class DateConvertionUtils {

    /**
     * Converts a ZonedDateTime to Date. Zone info is lost during the conversion.
     *
     * @param zonedDateTime ZonedDateTime to be converted to Date.
     * @return Date since epoch.
     */
    public static Date toDate(ZonedDateTime zonedDateTime) {
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * Converts a Date to ZonedDateTime using System Default as ZoneId.
     *
     * @param date to be converted.
     * @return ZonedDateTime based on date parameter.
     */
    public static ZonedDateTime toZonedDateTime(Date date) {
        return ZonedDateTime.ofInstant(date.toInstant(), ZoneOffset.systemDefault());
    }

    /**
     * Converts a sql Timestamp to ZonedDateTime using System Default as zone ID.
     *
     * @param timestamp Timestamp to be converted to ZonedDateTime
     * @return ZonedDateTime
     */
    public static ZonedDateTime toZonedDateTime(Timestamp timestamp) {
        return timestamp.toLocalDateTime().atZone(ZoneOffset.systemDefault());
    }

    /**
     * Converts a LocalDateTime to Date. Before the conversion the zone ID of the dateTime parameter
     * is set to System Default.
     *
     * @param dateTime LocalDateTime to be converted to Date.
     * @return Date since epoch.
     */
    public static Date toDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneOffset.systemDefault()).toInstant());
    }

    /**
     * Converts a Date to LocalDateTime using System Default as ZoneId.
     *
     * @param date to be converted.
     * @return LocalDateTime based on date parameter.
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.systemDefault());
    }

    /**
     * Converts a LocalDate to Date. Before the conversion the zone ID of the dateTime parameter
     * is set to System Default.
     *
     * @param localDate LocalDate to be converted to Date.
     * @return Date since epoch.
     */
    public static Date toDate(LocalDate localDate) {
        return toDate(localDate.atStartOfDay());
    }

    /**
     * Converts a Date to LocalDate using System Default as ZoneId.
     *
     * @param date to be converted.
     * @return LocalDate based on date parameter.
     */
    public static LocalDate toLocalDate(Date date) {
        return toLocalDateTime(date).toLocalDate();
    }
}
