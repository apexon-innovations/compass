package com.apexon.compass.clientdashboardservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
public class DayWiseDataDto {

    private Integer day;

    private Integer openTillNowPoints;

    private Integer completedPoints;

    private Integer reopenPoints;

    private Integer newlyAddedPoints;

    private Integer openTillNowCount;

    private Integer completedCount;

    private Integer reopenCount;

    private Integer newlyAddedCount;

    private Integer idealCompletedCount;

    private Integer idealCompletedPoints;

}
