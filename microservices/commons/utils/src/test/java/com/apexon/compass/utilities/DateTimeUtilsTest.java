package com.apexon.compass.utilities;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

import static com.apexon.compass.constants.DateTimeUtilsConstatnts.*;
import static com.apexon.compass.constants.PsrServiceConstants.NAINITIAL;
import static org.junit.jupiter.api.Assertions.*;

public class DateTimeUtilsTest {

    public DateTimeUtilsTest() {
    }

    @BeforeAll
    public static void init() {
        Mockito.mock(DateTimeUtils.class);
    }

    @Test
    void getDifferenceFromCurrentDateTest() {
        Date date = new Date(Instant.now().plus(5, ChronoUnit.MINUTES).getLong(ChronoField.NANO_OF_SECOND));
        long data = date.getTime();
        long data2 = new Date(Instant.now().toEpochMilli()).getTime();
        long excepted = data - data2;
        long actual = DateTimeUtils.getDifferenceFromCurrentDate(date);
        assertEquals(excepted, actual);
    }

    @Test
    void getStartDateByQuarterOneTest() {
        LocalDate expectedDate = LocalDate.of(2020, 1, 1);
        assertEquals(expectedDate, DateTimeUtils.getStartDateByQuarter(2020, 1));
    }

    @Test
    void getStartDateByQuarterTwoTest() {
        LocalDate expectedDate = LocalDate.of(2020, 4, 1);
        assertEquals(expectedDate, DateTimeUtils.getStartDateByQuarter(2020, 2));
    }

    @Test
    void getStartDateByQuarterThreeTest() {
        LocalDate expectedDate = LocalDate.of(2020, 7, 1);
        assertEquals(expectedDate, DateTimeUtils.getStartDateByQuarter(2020, 3));
    }

    @Test
    void getStartDateByQuarterFourTest() {
        LocalDate expectedDate = LocalDate.of(2020, 10, 1);
        assertEquals(expectedDate, DateTimeUtils.getStartDateByQuarter(2020, 4));
    }

    @Test
    void getStartDateByQuarterFiveTest() {
        LocalDate expectedDate = LocalDate.of(2020, 10, 01);
        assertThrows(IllegalArgumentException.class, () -> DateTimeUtils.getStartDateByQuarter(2020, 5));
    }

    @Test
    void getEndDateByQuarterStartDateJanMonthTest() {
        LocalDate expectedDate = LocalDate.of(2020, 3, 31);
        assertEquals(expectedDate, DateTimeUtils.getEndDateByQuarter(LocalDate.of(2020, 1, 1), 2020));
    }

    @Test
    void getEndDateByQuarterStartDateAprilMonthTest() {
        LocalDate expectedDate = LocalDate.of(2020, 6, 30);
        assertEquals(expectedDate, DateTimeUtils.getEndDateByQuarter(LocalDate.of(2020, 4, 1), 2020));
    }

    @Test
    void getEndDateByQuarterStartDateJulyMonthTest() {
        LocalDate expectedDate = LocalDate.of(2020, 9, 30);
        assertEquals(expectedDate, DateTimeUtils.getEndDateByQuarter(LocalDate.of(2020, 7, 1), 2020));
    }

    @Test
    void getEndDateByQuarterStartDateOctMonthTest() {
        LocalDate expectedDate = LocalDate.of(2020, 12, 31);
        assertEquals(expectedDate, DateTimeUtils.getEndDateByQuarter(LocalDate.of(2020, 10, 1), 2020));
    }

    @Test
    void getEndDateByQuarterStartDateNovMonthTest() {
        LocalDate expectedDate = LocalDate.of(2021, 1, 31);
        assertEquals(expectedDate, DateTimeUtils.getEndDateByQuarter(LocalDate.of(2020, 11, 24), 2020));
    }

    @Test
    void getEndDateByQuarterStartDateFebLeapYearMonthTest() {
        LocalDate expectedDate = LocalDate.of(2020, 4, 30);
        assertEquals(expectedDate, DateTimeUtils.getEndDateByQuarter(LocalDate.of(2020, 2, 29), 2020));
    }

    @Test
    void convertLocalDateToDateEndOfDayTest() {
        LocalDate actualDate = LocalDate.of(2020, 4, 30);
        Date expectedDate = Date.from(actualDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());
        assertEquals(expectedDate, DateTimeUtils.convertLocalDateToDate(actualDate, true));
    }

    @Test
    void convertLocalDateToDateStartOfDayTest() {
        LocalDate actualDate = LocalDate.of(2020, 4, 30);
        Date expectedDate = Date.from(actualDate.atTime(0, 0, 0).atZone(ZoneId.systemDefault()).toInstant());
        assertEquals(expectedDate, DateTimeUtils.convertLocalDateToDate(actualDate, false));
    }

    @Test
    void getTimeRatioTest() {
        Long startDate = ZonedDateTime.now().toInstant().minus(3, ChronoUnit.DAYS).toEpochMilli();
        Long endDate = ZonedDateTime.now().toInstant().plus(5, ChronoUnit.DAYS).toEpochMilli();
        int totalExpectedDays = (int) ((endDate - startDate) / TOTAL_SECONDS_OF_DAY);
        int currentDate = (int) ((ZonedDateTime.now().toInstant().toEpochMilli() - startDate) / TOTAL_SECONDS_OF_DAY);
        BigDecimal elapsedPercentage = (BigDecimal.valueOf((double) (currentDate * 100) / totalExpectedDays))
            .setScale(2, RoundingMode.HALF_DOWN);
        String expected = elapsedPercentage.intValue() > 100 ? 100 + "%" : elapsedPercentage + "%";
        assertEquals(expected, DateTimeUtils.getTimeRatio(startDate, endDate));

        assertThrows(NumberFormatException.class, () -> DateTimeUtils.getTimeRatio(startDate, startDate));
        // assertEquals(expected, );
    }

    @Test
    void getDaysFromDatesTest() {
        Long startDate = ZonedDateTime.now().toInstant().minus(3, ChronoUnit.DAYS).toEpochMilli();
        Long endDate = ZonedDateTime.now().toInstant().plus(7, ChronoUnit.DAYS).toEpochMilli();
        Integer expectedResult = 7;
        assertEquals(expectedResult, DateTimeUtils.getDaysFromDates(startDate, endDate));
    }

    @Test
    void epochToMonthDateTest() {
        String exceptedResult = LocalDate.now().getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault())
                + NAINITIAL + LocalDate.now().getDayOfMonth();
        assertEquals(exceptedResult,
                DateTimeUtils.epochToMonthDate(ZonedDateTime.now(ZoneOffset.UTC).toInstant().toEpochMilli()));
    }

    @Test
    void convertDateToStringTest() {
        String exceptedResult = DateTimeFormatter.ofPattern(SIMPLE_DATE_FORMAT).format(LocalDate.now());
        String actual = DateTimeUtils.convertDateToString(new Date(System.currentTimeMillis()));
        assertEquals(exceptedResult, actual);
    }

    @Test
    void convertDateToMonthTest() {
        String exceptedResult = DateTimeFormatter.ofPattern(MONTH_YEAR_FORMAT).format(LocalDate.now());
        String actual = DateTimeUtils.convertDateToMonth(new Date(System.currentTimeMillis()));
        assertEquals(exceptedResult, actual);
    }

    @Test
    void convertDateToWeekTest() {
        String exceptedResult = DateTimeFormatter.ofPattern(MONTH_DATE_FORMAT).format(LocalDate.now());
        String actual = DateTimeUtils.convertDateToWeek(new Date(System.currentTimeMillis()));
        assertEquals(exceptedResult, actual);
    }

    @Test
    void convertDateToTimeStampTest() {
        String exceptedResult = DateTimeFormatter.ofPattern(DATE_TIMESTAMP_FORMAT)
            .withZone(ZoneId.systemDefault())
            .format(new Date().toInstant());
        long date = ZonedDateTime.now().toInstant().toEpochMilli();
        String actual = DateTimeUtils.convertDateToTimeStamp(new Date(date));
        assertEquals(exceptedResult, actual);
    }

    @Test
    void convertMinutesToWorkingDaysTest() {
        String exceptedResult = "1d";
        assertEquals(exceptedResult, DateTimeUtils.convertMinutesToWorkingDays(480));
        exceptedResult = "2d";
        assertEquals(exceptedResult, DateTimeUtils.convertMinutesToWorkingDays(960));
        assertNull(DateTimeUtils.convertMinutesToWorkingDays(null));
    }

}
