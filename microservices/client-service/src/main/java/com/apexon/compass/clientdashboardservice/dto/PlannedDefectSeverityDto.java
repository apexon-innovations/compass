package com.apexon.compass.clientdashboardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlannedDefectSeverityDto {

    private Integer totalPlanned;

    private Integer openMajor;

    private Integer openMinor;

    private Integer openBlocker;

    private Integer openCritical;

}
