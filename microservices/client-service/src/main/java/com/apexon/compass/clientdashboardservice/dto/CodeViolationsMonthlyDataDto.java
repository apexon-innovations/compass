package com.apexon.compass.clientdashboardservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CodeViolationsMonthlyDataDto {

    private String month;

    private ViolationsDto violations;

}
