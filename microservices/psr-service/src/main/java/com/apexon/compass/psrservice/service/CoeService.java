package com.apexon.compass.psrservice.service;

import com.apexon.compass.psrservice.dto.CriteriawiseCoeDto;
import com.apexon.compass.psrservice.dto.TeamwiseCOEDto;

import java.util.List;

public interface CoeService {

    List<CriteriawiseCoeDto> getCoeCount(String propertyName);

    List<TeamwiseCOEDto> getTeamSizeDetails();

}
