package com.apexon.compass.clientdashboardservice.utils;

import static lombok.AccessLevel.PRIVATE;

import com.apexon.compass.utilities.ArithmeticUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class DateTimeUtils {

    private static String DATE_FORMAT = "MMM-YYYY";

    public static String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public static String formatMonth(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-yyyy");
        Date dateValue = simpleDateFormat.parse(date);
        SimpleDateFormat output = new SimpleDateFormat("MMM-YYYY");
        return output.format(dateValue);
    }

    public static Long getLastDayOfMonth(LocalDate date) {
        LocalDate lastDayOfMonth = date.withDayOfMonth(date.getMonth().length(date.isLeapYear()));
        return lastDayOfMonth.atTime(23, 59, 59).toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public static Long getLastDayOfMonthAtStart(LocalDate date) {
        LocalDate lastDayOfMonth = date.withDayOfMonth(date.getMonth().length(date.isLeapYear()));
        return lastDayOfMonth.atTime(00, 00, 00).toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public static Long getFirstDayOfMonth(int monthCount) {
        LocalDate firstDayOfMonth = LocalDate.now().minusMonths(monthCount).withDayOfMonth(1);
        return firstDayOfMonth.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
    }

    public static List<LocalDate> getStartDayOfMonths(LocalDate today, int duration) {
        LocalDate lastMonth = today.minusMonths(duration);
        LocalDate firstDay = lastMonth.withDayOfMonth(1);
        List<LocalDate> dates = new ArrayList<>();
        for (int i = 0; i < duration; i++) {
            dates.add(firstDay);
            firstDay = firstDay.plusMonths(1);
        }
        return dates;
    }

    public static Double convertToDays(Double value) {
        return ArithmeticUtils.round(value / 480, 2);
    }

}
