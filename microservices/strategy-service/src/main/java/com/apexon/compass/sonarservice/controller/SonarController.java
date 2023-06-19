package com.apexon.compass.sonarservice.controller;

import com.apexon.compass.exception.custom.RecordNotFoundException;
import com.apexon.compass.exception.custom.ServiceException;
import com.apexon.compass.exception.handler.ApiExceptionHandler;
import com.apexon.compass.sonarservice.dto.SearchParametersDto;
import com.apexon.compass.sonarservice.dto.SecurityDto;
import com.apexon.compass.sonarservice.service.SecurityStatService;
import com.apexon.compass.sonarservice.service.SonarService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.apexon.compass.constants.StrategyServiceRouteConstants.*;

@RestController
@RequestMapping(PROJECT_STRATEGIC_BOARD)
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
public class SonarController extends ApiExceptionHandler {

    private SonarService sonarService;

    private SecurityStatService securityStatService;

    @GetMapping(PROJECT_ID)
    @ResponseStatus(HttpStatus.OK)
    public SearchParametersDto getStrategicDetail(@PathVariable("projectId") String projectId,
            @RequestParam(value = "search", required = false) String search)
            throws ServiceException, RecordNotFoundException {
        return sonarService.getSonarStats(projectId, search);
    }

    @GetMapping(SECURITY_REPORT)
    @ResponseStatus(HttpStatus.OK)
    public SecurityDto getSecurityReport(@PathVariable("id") String projectId) throws ServiceException {
        return securityStatService.getSecurity(projectId);
    }

}
