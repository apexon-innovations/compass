package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCompletionSprintDataVo {

    private Integer sprintId;

    private Double totalSquads;

    private String name;

    private Integer completedPoints;

    private Integer completedCounts;

    private Integer projectId;

}
