package com.apexon.compass.clientdashboardservice.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SprintDayWiseDataDto {

    private Integer sprintId;

    private String name;

    private List<DayWiseDataDto> dayWiseData;

}
