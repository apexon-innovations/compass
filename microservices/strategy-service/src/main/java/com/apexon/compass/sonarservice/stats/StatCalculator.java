package com.apexon.compass.sonarservice.stats;

import com.apexon.compass.sonarservice.dto.SearchParametersDto;
import com.apexon.compass.entities.Project;

public interface StatCalculator {

    SearchParametersDto calculate(Project project);

}
