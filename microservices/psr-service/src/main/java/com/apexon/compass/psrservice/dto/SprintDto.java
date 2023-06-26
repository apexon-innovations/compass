package com.apexon.compass.psrservice.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SprintDto {

    private String id;

    private String name;

    private Long startDate;

    private Long endDate;

    private Integer daysLeft;

    private Integer hoursLeft;

    private List<DailyStatusDto> dailyStatus;

}
