package com.apexon.compass.clientdashboardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlannedBacklogOpenDefectsDto {

    private Integer total;

    private Integer critical;

    private Integer blocker;

    private Integer major;

    private Integer minor;

}
