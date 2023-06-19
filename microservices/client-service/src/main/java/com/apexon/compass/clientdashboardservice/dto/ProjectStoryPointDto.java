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
public class ProjectStoryPointDto {

    private String projectName;

    private String projectId;

    private Integer jiraProjectId;

    private Integer totalStoryPoints;

    private Double averageRatio;

    private List<SprintDataDto> sprintData;

}
