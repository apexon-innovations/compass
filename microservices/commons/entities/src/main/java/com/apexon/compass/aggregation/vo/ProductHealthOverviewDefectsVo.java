package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductHealthOverviewDefectsVo {

    private Double _id;

    private Double totalDefects;

    private Double resolvedDefects;

    private Double inProgressDefects;

    private Double backlogDefects;

}
