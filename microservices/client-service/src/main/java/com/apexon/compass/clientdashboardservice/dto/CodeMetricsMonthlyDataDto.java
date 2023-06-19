package com.apexon.compass.clientdashboardservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CodeMetricsMonthlyDataDto {

    private String month;

    private Integer addedLineOfCode;

    private Integer removedLineOfCode;

}
