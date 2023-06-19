package com.apexon.compass.psrservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Allocation {

    private String billable;

    private String startDate;

    private String endDate;

    private String totalDays;

    private String client;

    private String project;

}
