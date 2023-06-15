package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlannedBacklogOpenDefectsVo {

    private Double _id;

    private Double totalOpened;

    private Double critical;

    private Double blocker;

    private Double major;

    private Double minor;

}
