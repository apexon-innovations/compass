package com.apexon.compass.psrservice.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LeaveDataDto {

    private Long date;

    private Boolean isPublicHoliday;

    private String publicHolidayLocation;

    private Integer publicHolidayHours;

    private Boolean isMemberLeave;

    private Integer memberLeaveHours;

    private List<String> membersOnLeave;

}
