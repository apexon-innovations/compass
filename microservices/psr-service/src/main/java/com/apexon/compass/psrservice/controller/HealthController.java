package com.apexon.compass.psrservice.controller;

import static com.apexon.compass.constants.PsrServiceRouteConstants.OVERALL_HEALTH;

import com.apexon.compass.psrservice.dto.ProjectsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.apexon.compass.exception.handler.ApiExceptionHandler;
import com.apexon.compass.psrservice.service.ProjectService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = OVERALL_HEALTH)
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
public class HealthController extends ApiExceptionHandler {

    private ProjectService projectService;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public ProjectsDto getOverAllHealth(@RequestHeader(value = "X-User-Header", required = false) String emailAddress) {
        return ProjectsDto.builder().projects(projectService.getAllProjectDetails(emailAddress)).build();
    }

}
