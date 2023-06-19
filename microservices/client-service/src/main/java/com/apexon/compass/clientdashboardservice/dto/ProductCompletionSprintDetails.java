package com.apexon.compass.clientdashboardservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductCompletionSprintDetails {

    private Integer id;

    private String name;

    private Integer completedPoints;

    private Integer completedCount;

    private Integer squads;

}
