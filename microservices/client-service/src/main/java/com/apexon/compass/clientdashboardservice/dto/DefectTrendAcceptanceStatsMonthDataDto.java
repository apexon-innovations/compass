package com.apexon.compass.clientdashboardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DefectTrendAcceptanceStatsMonthDataDto {

    private String month;

    private Integer actedUpon;

    private Integer accepted;

    private Double ratio;

}
