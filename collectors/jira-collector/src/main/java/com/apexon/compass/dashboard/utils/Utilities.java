package com.apexon.compass.dashboard.utils;

import org.json.simple.JSONObject;

public class Utilities {

	/**
	 * Returns String.valueOf for the value.
	 */
	public static String getString(JSONObject json, String key) {
		if (json == null) {
			return "";
		}

		Object value = json.get(key);
		if (value == null) {
			return "";
		}

		if (value instanceof Double) {
			Double casted = (Double) value;
			return String.valueOf(casted.intValue());
		}

		return String.valueOf(value);
	}

	/**
	 * Converts the value to a long.
	 */
	public static long getLong(JSONObject json, String key) {
		if (json == null) {
			return 0;
		}

		Object value = json.get(key);
		if (value == null) {
			return 0;
		}

		return (Long) value;
	}

	/**
	 * Returns String.valueOf for the value.
	 */
	public static Boolean getBoolean(JSONObject json, String key) {
		if (json == null) {
			return false;
		}

		Object value = json.get(key);
		if (value == null) {
			return false;
		}

		return Boolean.valueOf(String.valueOf(value));
	}

	/**
	 * This is weird but way faster than Java date time formatter etc.
	 */
	public static String parseDateWithoutFraction(String date) {
		if (date == null) {
			return "";
		}

		if (date.length() < 20) {
			return date;
		}

		return date.substring(0, 19);
	}

}
