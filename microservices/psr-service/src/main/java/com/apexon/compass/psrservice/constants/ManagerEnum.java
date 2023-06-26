package com.apexon.compass.psrservice.constants;

public enum ManagerEnum {

    DELIVERY_MANAGER("deliveryManager"), ACCOUNT_MANAGER("accountManager");

    private String managerType;

    private ManagerEnum(String managerType) {
        this.managerType = managerType;
    }

    public String getManagerType() {
        return managerType;
    }

}
