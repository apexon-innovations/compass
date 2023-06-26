package com.apexon.compass.psrservice.service;

import com.apexon.compass.psrservice.dto.WeeklyStatusWrapperDto;

public interface WeeklyStatusService {

    WeeklyStatusWrapperDto getWeeklyProjectStatus(int year);

}
