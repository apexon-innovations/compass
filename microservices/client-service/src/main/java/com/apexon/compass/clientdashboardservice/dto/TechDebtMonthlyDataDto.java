package com.apexon.compass.clientdashboardservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TechDebtMonthlyDataDto {

    private String month;

    private Double security;

    private Double complexity;

    private Double issues;

    private Double reliability;

    private Double size;

    private Double coverage;

    private Double duplication;

    private Double maintainability;

    private Double totalDays;

}
