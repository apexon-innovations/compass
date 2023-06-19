package com.apexon.compass.sonarservice.stats;

import com.apexon.compass.entities.Project;
import com.apexon.compass.sonarservice.dto.SearchParametersDto;
import org.springframework.stereotype.Component;

@Component
public class ComplianceStatCalculator implements StatCalculator {

    @Override
    public SearchParametersDto calculate(Project project) {

        return SearchParametersDto.builder().build();
    }

}
