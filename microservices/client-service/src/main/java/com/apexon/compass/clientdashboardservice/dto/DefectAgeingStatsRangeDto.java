package com.apexon.compass.clientdashboardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DefectAgeingStatsRangeDto {

    private String value;

    private Integer critical;

    private Integer blocker;

    private Integer major;

    private Integer minor;

    private Integer unattended;

}
