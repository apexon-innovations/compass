package com.apexon.compass.psrservice.service;

import com.apexon.compass.psrservice.dto.PeopleStatusWrapperDto;

public interface PeopleStatusTrendService {

    PeopleStatusWrapperDto getPeopleStatus(int year, int quarter);

}
