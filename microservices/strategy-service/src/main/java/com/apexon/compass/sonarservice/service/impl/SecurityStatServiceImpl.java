package com.apexon.compass.sonarservice.service.impl;

import java.util.List;

import com.apexon.compass.sonarservice.dto.SecurityDto;
import com.apexon.compass.sonarservice.dto.SecurityScoreDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.apexon.compass.entities.SecurityStats;
import com.apexon.compass.exception.custom.ServiceException;
import com.apexon.compass.sonarservice.repository.SecurityStatsRepository;
import com.apexon.compass.sonarservice.service.SecurityStatService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
public class SecurityStatServiceImpl implements SecurityStatService {

    private SecurityStatsRepository securityStatsRepository;

    @Override
    public SecurityDto getSecurity(String projectId) {

        List<SecurityStats> securityStats = securityStatsRepository.findByProjectId(projectId,
                Sort.by(Sort.Direction.DESC, "jenkinsUpdatedDateTime"));

        if (securityStats.isEmpty()) {
            throw new ServiceException("Please enter valid project key");
        }

        return SecurityDto.builder()
            .securityScore(new SecurityScoreDto("0", "0", "0", "0", "0", "0", "0", "0"))
            .id(securityStats.get(0).getId())
            .name(securityStatsRepository
                .findByProjectId(projectId, Sort.by(Sort.Direction.DESC, "jenkinsUpdatedDateTime"))
                .get(0)
                .getProjectId())
            .build();
    }

}
