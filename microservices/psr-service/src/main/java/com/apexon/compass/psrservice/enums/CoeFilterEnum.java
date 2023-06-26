package com.apexon.compass.psrservice.enums;

public enum CoeFilterEnum {

    INDUSTRY("industrySpecificExposure"), SOLUTION("solutionTypeDistribution");

    private String type;

    private CoeFilterEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
