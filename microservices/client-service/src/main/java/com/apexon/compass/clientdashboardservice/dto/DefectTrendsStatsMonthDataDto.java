package com.apexon.compass.clientdashboardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DefectTrendsStatsMonthDataDto {

    private String month;

    private Integer openDefects;

    private Integer closedDefects;

    private Integer raisedDefects;

}
