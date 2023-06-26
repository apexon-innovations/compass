package com.apexon.compass.sonarservice.service;

import com.apexon.compass.sonarservice.dto.SecurityDto;

public interface SecurityStatService {

    SecurityDto getSecurity(String projectId);

}
