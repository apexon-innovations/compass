package com.apexon.compass.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import static com.apexon.compass.constants.PsrServiceConstants.NA;
import static com.apexon.compass.constants.PsrServiceConstants.AINITIAL;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArithmeticUtils {

    public static BigDecimal getRatioOfInteger(Integer int1, Integer int2) throws NumberFormatException {
        return BigDecimal.valueOf((double) (int1 * 100) / int2).setScale(2, RoundingMode.HALF_DOWN);
    }

    public static final Integer getMatcherCount(String matcher, String overAllHealth) {
        if (NA.equals(matcher) && AINITIAL.equals(overAllHealth)) {
            return 0;
        }
        return matcher.indexOf(overAllHealth) + 1;
    }

    public static String replaceNonNumericsWithZero(String input) {
        return input.replaceAll("^-?[^0-9.]*?$", "0");
    }

    public static double round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
