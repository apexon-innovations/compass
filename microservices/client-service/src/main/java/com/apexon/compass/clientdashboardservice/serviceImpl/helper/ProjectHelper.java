package com.apexon.compass.clientdashboardservice.serviceImpl.helper;

import com.apexon.compass.clientdashboardservice.dao.ProjectDetailsDao;
import com.apexon.compass.entities.Project;
import com.apexon.compass.exception.custom.ForbiddenException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class ProjectHelper {

    private ProjectDetailsDao projectDetailsDao;

    public Project findAndValidateProjectByIdAndUser(String id) {

        Project projects = projectDetailsDao.findAssociatedUsersWithProject(id,
                SecurityContextHolder.getContext().getAuthentication().getName());
        if (projects == null) {
            throw new ForbiddenException("Invalid access");
        }
        return projects;
    }

    public String getUserNameFromToken() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
