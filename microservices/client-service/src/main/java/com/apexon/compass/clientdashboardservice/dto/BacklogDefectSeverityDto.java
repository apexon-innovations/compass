package com.apexon.compass.clientdashboardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BacklogDefectSeverityDto {

    private Integer totalBacklog;

    private Integer unattendedMajor;

    private Integer unattendedMinor;

    private Integer unattendedBlocker;

    private Integer unattendedCritical;

}
