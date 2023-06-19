package com.apexon.compass.psrservice.controller;

import static com.apexon.compass.constants.PsrServiceConstants.PROJECT_ID_CAPITALIZED;
import static com.apexon.compass.constants.PsrServiceRouteConstants.PROJECTID;
import static com.apexon.compass.constants.PsrServiceRouteConstants.PROJECT_DETAILS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.apexon.compass.exception.handler.ApiExceptionHandler;
import com.apexon.compass.psrservice.dto.OverallHealthDto;
import com.apexon.compass.psrservice.service.ProjectService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(PROJECT_DETAILS)
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
public class OperationalController extends ApiExceptionHandler {

    private ProjectService projectService;

    @GetMapping(path = PROJECTID)
    @ResponseStatus(value = HttpStatus.OK)
    public OverallHealthDto getProjectDetails(@PathVariable(PROJECT_ID_CAPITALIZED) String projectId) {
        return projectService.getProjectDetails(projectId);
    }

}
