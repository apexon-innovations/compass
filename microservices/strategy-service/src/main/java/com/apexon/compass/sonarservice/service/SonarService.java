package com.apexon.compass.sonarservice.service;

import com.apexon.compass.sonarservice.dto.SearchParametersDto;

public interface SonarService {

    SearchParametersDto getSonarStats(String projectId, String search);

}
