package com.apexon.compass.clientdashboardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DefectTrendsTypeDto {

    private String id;

    private String name;

    private Integer jiraProjectId;

    private Integer totalDefects;

    private Integer acceptedDefects;

    private String acceptedDefectsStatus;

    private Integer openDefects;

    private Integer reopenedDefects;

    private Integer closedDefects;

    private Integer unattendedDefects;

    private String unattendedDefectsStatus;

    private Integer rejectedDefects;

    private String rejectedDefectsStatus;

}
