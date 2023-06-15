package com.apexon.compass.utilities;

import static com.apexon.compass.constants.CompassUtilityConstants.EMAIL_REGX;
import static com.apexon.compass.constants.StrategyServiceConstants.HOURS;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringOperationsUtils {

    private static final String REGEX_CSV = "\\s*,\\s*";

    public static String concatString(List<String> status) {
        return status.stream().map(s -> "\"" + s + "\"").collect(Collectors.joining(","));
    }

    public static String concatHourToInteger(Integer value) {
        return value + StringUtils.SPACE + HOURS;
    }

    public static boolean isEmail(String email) {
        return Pattern.compile(EMAIL_REGX).matcher(email).matches();
    }

    public static String createInitials(String str) {
        if (StringUtils.isEmpty(str)) {
            return StringUtils.EMPTY;
        }
        String[] splits = str.split("\\s+");
        StringBuilder initials = new StringBuilder();
        for (String s : splits) {
            String split = s.replaceAll("[^a-zA-Z0-9]", "");
            if (StringUtils.isNotEmpty(split)) {
                if (initials.length() == 0) {
                    initials.append(StringUtils.SPACE);
                }
                initials.append(Character.toUpperCase(split.charAt(0)));
            }
        }
        return initials.toString();
    }

    public static List<String> csvToList(String csv) {
        checkNotNull(csv);
        return Arrays.asList(csv.split(REGEX_CSV));
    }

    public static String listToCsv(List<String> list) {
        checkNotNull(list);
        return String.join(",", list);
    }

    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        else {
            return reference;
        }
    }

}
