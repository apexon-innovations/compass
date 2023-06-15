package com.apexon.compass.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DefectAgeingTrendsDataList {

    private Integer min;

    private Integer max;

    private Integer count;

    private Integer major;

    private Integer minor;

    private Integer blocker;

    private Integer critical;

    private Integer unattended;

}
