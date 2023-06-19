package com.apexon.compass.psrservice.enums;

public enum AcceptedExcelTypes {

    XLSX("xlsx"), XLSM("xlsm");

    private final String value;

    AcceptedExcelTypes(String value) {
        this.value = value;
    }

    public static boolean contains(String value) {
        for (AcceptedExcelTypes val : values()) {
            if (val.value.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public String getValue() {
        return value;
    }

}
