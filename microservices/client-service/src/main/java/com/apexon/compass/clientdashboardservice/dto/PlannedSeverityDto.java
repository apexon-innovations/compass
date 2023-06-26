package com.apexon.compass.clientdashboardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlannedSeverityDto {

    private Integer totalPlanned;

    private Integer critical;

    private Integer blocker;

    private Integer major;

    private Integer minor;

}
