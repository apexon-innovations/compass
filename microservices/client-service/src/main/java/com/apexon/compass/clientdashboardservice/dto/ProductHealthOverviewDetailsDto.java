package com.apexon.compass.clientdashboardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductHealthOverviewDetailsDto {

    private Integer projectId;

    private String projectName;

    private Integer totalStories;

    private Integer completedStories;

    private Integer pendingStories;

    private Integer backlogStories;

    private Integer totalDefects;

    private Integer resolvedDefects;

    private Integer pendingDefects;

    private Integer backlogDefects;

}
