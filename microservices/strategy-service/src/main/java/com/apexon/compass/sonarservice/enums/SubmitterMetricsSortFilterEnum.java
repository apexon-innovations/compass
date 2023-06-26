package com.apexon.compass.sonarservice.enums;

public enum SubmitterMetricsSortFilterEnum {

    TOP("top", "totalPrs"), MEMBERWISE("memberwise", "_id"), MOSTRECOMMITS("mostRecommits", "recommits"),
    MOSTCOMMENTS("mostComments", "reviewerComments");

    private final String key;

    private final String value;

    SubmitterMetricsSortFilterEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static SubmitterMetricsSortFilterEnum findByKey(String key) {

        for (SubmitterMetricsSortFilterEnum val : values()) {
            if (val.getKey().equals(key)) {
                return val;
            }
        }

        return null;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

}
