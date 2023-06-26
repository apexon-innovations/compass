package com.apexon.compass.psrservice.controller;

import static com.apexon.compass.constants.PsrServiceRouteConstants.COE_DETAILS;
import static com.apexon.compass.constants.PsrServiceRouteConstants.INDUSTRY_WISE;
import static com.apexon.compass.constants.PsrServiceRouteConstants.SOLUTION_WISE;
import static com.apexon.compass.constants.PsrServiceRouteConstants.TEAM_WISE;

import com.apexon.compass.exception.handler.ApiExceptionHandler;
import com.apexon.compass.psrservice.dto.CriteriawiseCoeDto;
import com.apexon.compass.psrservice.dto.TeamwiseCOEDto;
import com.apexon.compass.psrservice.enums.CoeFilterEnum;
import com.apexon.compass.psrservice.service.CoeService;
import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(COE_DETAILS)
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
public class CoeController extends ApiExceptionHandler {

    private CoeService coeService;

    @GetMapping(TEAM_WISE)
    @ResponseStatus(value = HttpStatus.OK)
    public List<TeamwiseCOEDto> getTeamSize() {
        return coeService.getTeamSizeDetails();
    }

    @GetMapping(INDUSTRY_WISE)
    @ResponseStatus(value = HttpStatus.OK)
    public List<CriteriawiseCoeDto> getIndustrialWiseCount() {
        return coeService.getCoeCount(CoeFilterEnum.INDUSTRY.getType());
    }

    @GetMapping(SOLUTION_WISE)
    @ResponseStatus(value = HttpStatus.OK)
    public List<CriteriawiseCoeDto> getSolutionWiseCount() {
        return coeService.getCoeCount(CoeFilterEnum.SOLUTION.getType());
    }

}
