package com.apexon.compass.clientdashboardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DefectTrendsAcceptanceDto {

    private String id;

    private String projectName;

    private Integer jiraProjectId;

    private Integer actedUpon;

    private Integer accepted;

    private Double ratio;

}
