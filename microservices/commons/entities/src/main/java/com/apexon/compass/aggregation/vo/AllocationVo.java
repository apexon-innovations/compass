package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllocationVo {

    private Long startDate;

    private Long endDate;

    private Integer totalDays;

    private Float percentage;

    private Boolean isBillable;

    private String projectRole;

}
