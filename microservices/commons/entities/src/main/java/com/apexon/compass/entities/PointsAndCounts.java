package com.apexon.compass.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointsAndCounts {

    private Double _id;

    private Integer plannedPoints;

    private Double completedPoints;

    private Double plannedCounts;

    private Double completedCounts;

}
