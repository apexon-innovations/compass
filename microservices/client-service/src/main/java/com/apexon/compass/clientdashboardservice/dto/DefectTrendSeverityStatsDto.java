package com.apexon.compass.clientdashboardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DefectTrendSeverityStatsDto {

    private String id;

    private String name;

    private Integer jiraProjectId;

    private List<DefectTrendSeverityStatsMonthDto> monthData;

}
