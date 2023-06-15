package com.apexon.compass.utilities;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArithmeticUtilsTest {

    @Test
    void getRatioOfIntegerTest() {
        int value1 = 368490;
        int value2 = value1 * 2;
        BigDecimal exceptedResult = new BigDecimal("50.00");
        assertEquals(exceptedResult, ArithmeticUtils.getRatioOfInteger(value1, value2));
        assertThrows(NumberFormatException.class, () -> ArithmeticUtils.getRatioOfInteger(value1, 0));
    }

    @Test
    void getMatcherCountTest() {
        Integer exceptedResult = 0;
        assertEquals(exceptedResult, ArithmeticUtils.getMatcherCount("G", "A"));
        exceptedResult = 1;
        assertEquals(exceptedResult, ArithmeticUtils.getMatcherCount("G", "G"));
    }

    @Test
    void replaceNonNumericsWithZeroTest() {
        String expectedResult = "0";
        assertEquals(expectedResult, ArithmeticUtils.replaceNonNumericsWithZero("ABCD"));
        assertEquals(expectedResult, ArithmeticUtils.replaceNonNumericsWithZero("A-B"));
    }

}
