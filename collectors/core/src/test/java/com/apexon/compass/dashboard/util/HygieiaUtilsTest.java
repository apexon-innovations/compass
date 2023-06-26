package com.apexon.compass.dashboard.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HygieiaUtilsTest {

	@Test
	void checkForEmptyStringValuesTest() {
		boolean result = HygieiaUtils.checkForEmptyStringValues("", "test1", "test2");
		assertTrue(result);

		result = HygieiaUtils.checkForEmptyStringValues("test0", "test1", "test2");
		assertFalse(result);

		assertEquals("https://jenkins.com/job/test/job/youseeme/job/Reference/job/master/", HygieiaUtils
			.normalizeJobUrl("https://jenkins.com/job/test/job/youseeme/job/Reference/job/master/228/"));
		assertEquals("https://jenkins.com/job/test/job/youseeme/job/Reference/job/master/",
				HygieiaUtils.normalizeJobUrl("https://jenkins.com/job/test/job/youseeme/job/Reference/job/master/"));
		assertEquals("https://jenkins.com/job/test/job/youseeme/job/Reference/job/master",
				HygieiaUtils.normalizeJobUrl("https://jenkins.com/job/test/job/youseeme/job/Reference/job/master"));

		assertTrue(HygieiaUtils.isNumeric("1252435"));
		assertFalse(HygieiaUtils.isNumeric("zbcxsfgde"));
		assertFalse(HygieiaUtils.isNumeric("a@#%#^^"));
		assertFalse(HygieiaUtils.isNumeric("12as%6"));
	}

}
