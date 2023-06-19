package com.apexon.compass.clientdashboardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DefectTrendSeverityStatsMonthDto {

    private String month;

    private PlannedSeverityDto planned;

    private BacklogSeverityDto backlog;

}
