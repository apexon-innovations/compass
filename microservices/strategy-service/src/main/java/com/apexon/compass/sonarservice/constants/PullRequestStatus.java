package com.apexon.compass.sonarservice.constants;

public enum PullRequestStatus {

    MERGED("merged"), OPEN("open"), DECLINED("declined");

    private final String value;

    PullRequestStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
