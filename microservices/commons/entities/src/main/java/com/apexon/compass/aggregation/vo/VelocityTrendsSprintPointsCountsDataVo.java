package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VelocityTrendsSprintPointsCountsDataVo {

    private Double _id;

    private Integer plannedPoints;

    private Double completedPoints;

    private Double plannedCounts;

    private Double completedCounts;

}
