package com.apexon.compass.psrservice.service;

import com.apexon.compass.exception.custom.ForbiddenException;
import com.apexon.compass.exception.custom.RecordNotFoundException;
import com.apexon.compass.exception.custom.UnauthorizedException;
import com.apexon.compass.psrservice.dto.*;

import java.util.List;

public interface ProjectService {

    List<OverallHealthDto> getAllProjectDetails(String emailAddress);

    List<OverallHealthDto> getClientProjectDetails(String emailAddress);

    OverallHealthDto getProjectDetails(String projectId);

    ManagerWrapperDto getStrategicDetailsOfManager(String propertyName);

    OverallProjectHealthWrapperDto getOverallProjectHealth();

    WeeklyCriteriaWrapperDto getCriteriaWiseHelath();

    NetPromoterWrapperDto getNpmScore();

    ProjectDataDto getProject(String id) throws UnauthorizedException, RecordNotFoundException, ForbiddenException;

    AllProjectsDto getAssignedProjects(String email) throws RecordNotFoundException;

    ComplianceDto getProjectCompliance(String id);

    AllClientProjectDto getClientProjects(String email) throws RecordNotFoundException;

}
