package com.apexon.compass.sonarservice.service.impl;

import com.apexon.compass.entities.Project;
import com.apexon.compass.sonarservice.dto.SearchParametersDto;
import com.apexon.compass.exception.custom.RecordNotFoundException;
import com.apexon.compass.sonarservice.repository.ProjectRepository;
import com.apexon.compass.sonarservice.service.SonarService;
import com.apexon.compass.sonarservice.stats.StatCalculator;
import com.apexon.compass.sonarservice.stats.StatCalculatorFactory;
import java.util.Arrays;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
public class SonarServiceImpl implements SonarService {

    private ProjectRepository projectRepository;

    private StatCalculatorFactory statCalculatorFactory;

    private Project getProject(String projectId) {

        Optional<Project> Projects = projectRepository.findByIdAndIsDeletedIn(projectId,
                Arrays.asList(null, Boolean.FALSE));
        return Projects.orElseThrow(() -> new RecordNotFoundException("Please enter valid id"));
    }

    @Override
    public SearchParametersDto getSonarStats(String projectId, String search) {
        Project projects = getProject(projectId);
        StatCalculator statCalculator = statCalculatorFactory.getStatCalculator(search);
        return statCalculator.calculate(projects);
    }

}
