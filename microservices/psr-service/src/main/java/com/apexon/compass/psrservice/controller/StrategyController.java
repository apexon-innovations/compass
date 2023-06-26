package com.apexon.compass.psrservice.controller;

import static com.apexon.compass.constants.PsrServiceRouteConstants.ACCOUNT_MANAGER;
import static com.apexon.compass.constants.PsrServiceRouteConstants.CRITERIA_WISE_HEALTH;
import static com.apexon.compass.constants.PsrServiceRouteConstants.DELIVERY_MANAGER;
import static com.apexon.compass.constants.PsrServiceRouteConstants.NET_PROMOTER;
import static com.apexon.compass.constants.PsrServiceRouteConstants.OVERALL_PROJECT_HEALTH;
import static com.apexon.compass.constants.PsrServiceRouteConstants.PEOPLE_STATUS_TREND;
import static com.apexon.compass.constants.PsrServiceRouteConstants.STRATEGIC_DETAILS;
import static com.apexon.compass.constants.PsrServiceRouteConstants.WEEKLY_STATUS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.apexon.compass.exception.handler.ApiExceptionHandler;
import com.apexon.compass.psrservice.dto.ManagerWrapperDto;
import com.apexon.compass.psrservice.dto.NetPromoterWrapperDto;
import com.apexon.compass.psrservice.dto.OverallProjectHealthWrapperDto;
import com.apexon.compass.psrservice.dto.PeopleStatusWrapperDto;
import com.apexon.compass.psrservice.dto.WeeklyCriteriaWrapperDto;
import com.apexon.compass.psrservice.dto.WeeklyStatusWrapperDto;
import com.apexon.compass.psrservice.enums.ManagerEnum;
import com.apexon.compass.psrservice.service.PeopleStatusTrendService;
import com.apexon.compass.psrservice.service.ProjectService;
import com.apexon.compass.psrservice.service.WeeklyStatusService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(STRATEGIC_DETAILS)
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
public class StrategyController extends ApiExceptionHandler {

    private ProjectService projectService;

    private PeopleStatusTrendService peopleStatusTrendService;

    private WeeklyStatusService weeklyStatusService;

    @GetMapping(path = NET_PROMOTER)
    @ResponseStatus(value = HttpStatus.OK)
    public NetPromoterWrapperDto getNetPromoterHealth() {
        return projectService.getNpmScore();
    }

    @GetMapping(path = DELIVERY_MANAGER)
    @ResponseStatus(value = HttpStatus.OK)
    public ManagerWrapperDto getDeliveryManagerDetails() {
        return projectService.getStrategicDetailsOfManager(ManagerEnum.DELIVERY_MANAGER.getManagerType());
    }

    @GetMapping(path = ACCOUNT_MANAGER)
    @ResponseStatus(value = HttpStatus.OK)
    public ManagerWrapperDto getAccountManagerDetails() {
        return projectService.getStrategicDetailsOfManager(ManagerEnum.ACCOUNT_MANAGER.getManagerType());
    }

    @GetMapping(path = PEOPLE_STATUS_TREND)
    @ResponseStatus(value = HttpStatus.OK)
    public PeopleStatusWrapperDto getProjectDetails(@PathVariable("year") int year,
            @RequestParam(value = "quarter", required = true) int quarter) {
        return peopleStatusTrendService.getPeopleStatus(year, quarter);
    }

    @GetMapping(path = WEEKLY_STATUS)
    @ResponseStatus(value = HttpStatus.OK)
    public WeeklyStatusWrapperDto getGraphData(@RequestParam int year) {
        return weeklyStatusService.getWeeklyProjectStatus(year);
    }

    @GetMapping(path = OVERALL_PROJECT_HEALTH)
    @ResponseStatus(value = HttpStatus.OK)
    public OverallProjectHealthWrapperDto getOverallHealth() {
        return projectService.getOverallProjectHealth();
    }

    @GetMapping(path = CRITERIA_WISE_HEALTH)
    @ResponseStatus(value = HttpStatus.OK)
    public WeeklyCriteriaWrapperDto getCriteriaWiseHelath() {
        return projectService.getCriteriaWiseHelath();
    }

}
