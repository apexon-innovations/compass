package com.apexon.compass.utilities;

import static com.apexon.compass.constants.CompassUtilityConstants.DATE_FORMAT_EXCEL;
import static com.apexon.compass.constants.DateTimeUtilsConstatnts.DATE_TIME_TO_EPOCH;
import static com.apexon.compass.constants.DateTimeUtilsConstatnts.GET_EPOC_TO_STRING;
import static com.apexon.compass.constants.DateTimeUtilsConstatnts.GET_LAST_DATE_OF_MONTH;
import static com.apexon.compass.constants.DateTimeUtilsConstatnts.GET_LOCAL_DATE_TO_STRING;
import static com.apexon.compass.constants.DateTimeUtilsConstatnts.GET_LONGDATE_TO_STRING;
import static com.apexon.compass.constants.DateTimeUtilsConstatnts.GET_LONG_TO_STRING;
import static com.apexon.compass.constants.DateTimeUtilsConstatnts.TOTAL_SECONDS_OF_DAY;
import static com.apexon.compass.constants.PsrServiceConstants.MONGO_DATE_FORMAT;
import static com.apexon.compass.constants.PsrServiceConstants.MONGO_DATE_TIMESTAMP_FORMAT;
import static com.apexon.compass.constants.PsrServiceConstants.MONGO_MONTH_FORMAT;
import static com.apexon.compass.constants.PsrServiceConstants.MONGO_WEEK_FORMAT;
import static com.apexon.compass.constants.PsrServiceConstants.NAINITIAL;
import static com.apexon.compass.constants.StrategyServiceConstants.DAYINITIAL;
import static com.apexon.compass.constants.StrategyServiceConstants.DAYS;
import static com.apexon.compass.constants.StrategyServiceConstants.HOURINITIAL;
import static com.apexon.compass.constants.StrategyServiceConstants.HOURS;
import static com.apexon.compass.constants.StrategyServiceConstants.MINUTEINITIAL;
import static com.apexon.compass.constants.StrategyServiceConstants.MINUTES;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeUtils {

    public static Long getDifferenceFromCurrentDate(Date date) {
        return date.getTime() - new Date(Instant.now().toEpochMilli()).getTime();
    }

    public static long dateTimeToEpoch(String dateTime) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_TO_EPOCH);
        Date date = df.parse(dateTime);
        return date.getTime();
    }

    public static LocalDate getLastDateOfMonth(String date) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern(GET_LAST_DATE_OF_MONTH);
        YearMonth yearMonth = YearMonth.parse(date, pattern);
        return yearMonth.atEndOfMonth();
    }

    public static String getLocalDatetoString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(GET_LOCAL_DATE_TO_STRING);
        return date.format(formatter);
    }

    public static String getLocalDateTimeToString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(GET_EPOC_TO_STRING);
        return date.format(formatter);
    }

    public static String getEpochDateToString(Long dateTime) {
        DateFormat format = new SimpleDateFormat(GET_LONGDATE_TO_STRING);
        return format.format(new Date(dateTime));
    }

    public static String getMonthAndYearFromLongDate(Long dateTime) {
        Date date = new Date(dateTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-yy");
        return dateFormat.format(date);
    }

    public static String getLongDateTimeToString(Long dateTime) {

        DateFormat format = new SimpleDateFormat(GET_LONG_TO_STRING);
        return format.format(new Date(dateTime));
    }

    public static String convertDateToString(Date date) {
        return MONGO_DATE_FORMAT.format(date);
    }

    public static String convertDateToMonth(Date date) {
        return MONGO_MONTH_FORMAT.format(date);
    }

    public static String convertDateToWeek(Date date) {
        return MONGO_WEEK_FORMAT.format(date);
    }

    public static String convertMilisToWeek(Long milisecond) {
        return MONGO_WEEK_FORMAT.format(milisecond);
    }

    public static LocalDate getStartDateByQuarter(int year, int quarter)
            throws DateTimeException, IllegalArgumentException {
        if (!(quarter > 0 && quarter < 5)) {
            throw new IllegalArgumentException("Invalide quater");
        }
        int month = ((quarter - 1) * 3) + 1;
        return LocalDate.of(year, month, 1);
    }

    public static LocalDate getEndDateByQuarter(LocalDate startDate, int year) throws DateTimeException {

        if (startDate.getMonthValue() >= 11) {
            year++;
        }
        Month endMonth = startDate.getMonth().plus(2);
        return LocalDate.of(year, endMonth, endMonth.length(startDate.isLeapYear()));
    }

    public static Date convertLocalDateToDate(LocalDate local, boolean endOfDay) throws IllegalArgumentException {
        return (endOfDay) ? Date.from(local.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant())
                : Date.from(local.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String convertDateToTimeStamp(Date date) {
        return MONGO_DATE_TIMESTAMP_FORMAT.format(date);
    }

    public static String getTimeRatio(Long startDate, Long endDate) throws NumberFormatException {
        int totalDays = (int) ((endDate - startDate) / TOTAL_SECONDS_OF_DAY);
        int currentDate = (int) ((ZonedDateTime.now().toInstant().toEpochMilli() - startDate) / TOTAL_SECONDS_OF_DAY);
        BigDecimal elapsedPercentage = (BigDecimal.valueOf((double) (currentDate * 100) / totalDays)).setScale(2,
                RoundingMode.HALF_DOWN);
        return elapsedPercentage.intValue() > 100 ? 100 + "%" : elapsedPercentage + "%";
    }

    public static Integer getDaysFromDates(Long startDate, Long endDate) {
        int totalDays = (int) ((endDate - startDate) / TOTAL_SECONDS_OF_DAY);
        int currentDate = (int) ((ZonedDateTime.now().toInstant().toEpochMilli() - startDate) / TOTAL_SECONDS_OF_DAY);
        return (totalDays - currentDate) < 0 ? 0 : (totalDays - currentDate);
    }

    public static String epochToMonthDate(Long date) {
        LocalDate localDate = Instant.ofEpochMilli(date).atZone(ZoneOffset.UTC).toLocalDate();
        return localDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()) + NAINITIAL
                + localDate.getDayOfMonth();
    }

    public static String convertMinutesToWorkingDays(Integer minutes) {
        return minutes == null ? null : String.valueOf(minutes / 8 / 60) + DAYINITIAL;
    }

    public static Integer getDaysLeft(Long end) {
        if (ZonedDateTime.now().toInstant().toEpochMilli() > end) {
            return 0;
        }
        LocalDate startDate = ZonedDateTime.now(ZoneOffset.UTC).toLocalDate();
        LocalDate endDate = Instant.ofEpochMilli(end).atZone(ZoneOffset.UTC).toLocalDate();
        DayOfWeek startW = startDate.getDayOfWeek();
        int days = (int) ChronoUnit.DAYS.between(startDate, endDate);
        return (days - 2 * ((days + startW.getValue()) / 7));
    }

    public static DayOfWeek getDayOfWeekFromLongDate(long date) {
        LocalDate startDates = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate();
        return startDates.getDayOfWeek();
    }

    public static long getWorkingDays(LocalDate startDate, LocalDate endDate) {
        final DayOfWeek startW = startDate.getDayOfWeek();
        final DayOfWeek endW = endDate.getDayOfWeek();
        final long days = ChronoUnit.DAYS.between(startDate, endDate);
        final long daysWithoutWeekends = days - 2 * ((days + startW.getValue()) / 7);
        return daysWithoutWeekends + (startW == DayOfWeek.SUNDAY ? 1 : 0) + (endW == DayOfWeek.SUNDAY ? 1 : 0);
    }

    public static long toEpochMilli(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
    }

    public static String getDurationInDays(Long startDate, Long endDate) {
        Long days = Math.max(TimeUnit.MILLISECONDS.toDays(endDate - startDate), 0);
        Long hours = Math.max((TimeUnit.MILLISECONDS.toHours(endDate - startDate)) - (days * 24), 0);
        Long minutes = Math.max(TimeUnit.MILLISECONDS.toMinutes(endDate - startDate)
                - (TimeUnit.MILLISECONDS.toHours(endDate - startDate) * 60), 0);
        if (days <= 0 && hours <= 0) {
            return minutes + MINUTEINITIAL;
        }
        else if (days <= 0) {
            return hours + HOURINITIAL + " " + minutes + MINUTEINITIAL;
        }
        else {
            return days + DAYINITIAL + " " + hours + HOURINITIAL + " " + minutes + MINUTEINITIAL;
        }
    }

    public static LocalDate convertDateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date convertStringToDate(String dateTime) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_EXCEL);
        return df.parse(dateTime);
    }

    public static String convertMinutesToDays(long minutes) {
        if (minutes < 0) {
            return 0 + StringUtils.SPACE + MINUTES;
        }
        long days = minutes / 480;
        long hours = Math.max((TimeUnit.MINUTES.toHours(minutes)) - (days * 8), 0);
        if (days <= 0 && hours <= 0) {
            return minutes + StringUtils.SPACE + MINUTES;
        }
        else if (days <= 0) {
            return hours + HOURS;
        }
        else {
            return hours <= 0 ? days + DAYS : days + DAYS + StringUtils.SPACE + hours + HOURS;
        }
    }

}
