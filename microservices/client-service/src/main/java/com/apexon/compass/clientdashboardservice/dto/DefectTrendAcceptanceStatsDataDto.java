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
public class DefectTrendAcceptanceStatsDataDto {

    private String projectId;

    private String projectName;

    private Integer jiraProjectId;

    private List<DefectTrendAcceptanceStatsMonthDataDto> monthData;

}
