package com.healthos.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * HealthOS Date and Time Formatting Utility.
 */
public final class DateUtil {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter FRIENDLY_DATE = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy", Locale.ENGLISH);
    public static final DateTimeFormatter SHORT_DATE = DateTimeFormatter.ofPattern("MMM d", Locale.ENGLISH);
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
    public static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("MMM d, yyyy hh:mm a", Locale.ENGLISH);

    private DateUtil() {}

    public static String formatFriendly(LocalDate date) {
        if (date == null) return "N/A";
        if (date.isEqual(LocalDate.now())) return "Today, " + date.format(SHORT_DATE);
        if (date.isEqual(LocalDate.now().minusDays(1))) return "Yesterday, " + date.format(SHORT_DATE);
        return date.format(FRIENDLY_DATE);
    }

    public static String formatShort(LocalDate date) {
        return date == null ? "" : date.format(SHORT_DATE);
    }

    public static String formatTime(LocalTime time) {
        return time == null ? "" : time.format(TIME_FORMATTER);
    }

    public static String formatTimestamp(LocalDateTime dateTime) {
        return dateTime == null ? "" : dateTime.format(TIMESTAMP_FORMATTER);
    }
}
