package com.apexon.compass.sonarservice.utils;

import static com.apexon.compass.constants.DateTimeUtilsConstatnts.DATE_RANGE_TO_TEXT;
import static com.apexon.compass.constants.DateTimeUtilsConstatnts.GET_LONG_TO_STRING;
import static com.apexon.compass.constants.StrategyServiceConstants.DAYS;
import static com.apexon.compass.constants.StrategyServiceConstants.HOURS;
import static com.apexon.compass.constants.StrategyServiceConstants.MINUTES;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateTimeUtils {

    public static String getDateRange(Long startDate, Long endDate) {
        LocalDate startLocalDate = getLocalDateFromLong(startDate);
        LocalDate endLocalDate = getLocalDateFromLong(endDate);
        return startLocalDate.format(DateTimeFormatter.ofPattern(GET_LONG_TO_STRING)) + DATE_RANGE_TO_TEXT
                + endLocalDate.format(DateTimeFormatter.ofPattern(GET_LONG_TO_STRING));
    }

    public static LocalDate getLocalDateFromLong(Long date) {
        return Instant.ofEpochMilli(date).atZone(ZoneOffset.UTC).toLocalDate();
    }

    public static String getDaysAndHoursFromMinutes(long minutes) {
        if (minutes < 0) {
            return 0 + StringUtils.SPACE + MINUTES;
        }
        long days = TimeUnit.MINUTES.toDays(minutes);
        long hours = Math.max((TimeUnit.MINUTES.toHours(minutes)) - (days * 24), 0);
        if (days <= 0 && hours <= 0) {
            return minutes + StringUtils.SPACE + MINUTES;
        }
        else if (days <= 0) {
            return hours + HOURS;
        }
        else {
            return days + DAYS;
        }
    }

    public static Long getPreviousNDaysDateAndTime(int dayCount) {
        LocalDate date = LocalDate.now().minusDays(dayCount);
        return date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
    }

}
