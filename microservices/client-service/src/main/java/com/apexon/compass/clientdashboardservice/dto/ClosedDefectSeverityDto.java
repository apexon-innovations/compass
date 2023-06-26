package com.apexon.compass.clientdashboardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClosedDefectSeverityDto {

    private Integer totalClosed;

    private Integer closeMajor;

    private Integer closeMinor;

    private Integer closeBlocker;

    private Integer closeCritical;

}
