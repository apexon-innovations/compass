package com.apexon.compass.psrservice.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneOffset;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeUtils {

    public static Long getFirstDayOfMonth(int monthCount) {
        LocalDate firstDayOfMonth = LocalDate.now().minusMonths(monthCount).withDayOfMonth(1);
        return firstDayOfMonth.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
    }

}
