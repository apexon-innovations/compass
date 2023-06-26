package com.apexon.compass.sonarservice.service.impl.helper;

import com.apexon.compass.entities.Project;
import com.apexon.compass.exception.custom.ForbiddenException;
import com.apexon.compass.sonarservice.configuration.JwtSecretProperty;
import com.apexon.compass.sonarservice.dao.ProjectDao;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class ProjectHelper {

    private final ProjectDao projectDao;

    private final JwtSecretProperty jwtSecretProperty;

    public Project findAndValidateProjectByIdAndUser(String projectId) {
        Project projects = projectDao.findAssociatedUsersWithProject(projectId,
                SecurityContextHolder.getContext().getAuthentication().getName());
        if (projects == null) {
            throw new ForbiddenException("Invalid access");
        }
        return projects;
    }

    public List<String> getWidget() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Arrays.asList(jwt.getClaimAsString("wdgt").split(","));
    }

}
