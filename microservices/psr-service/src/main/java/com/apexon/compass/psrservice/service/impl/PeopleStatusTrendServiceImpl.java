package com.apexon.compass.psrservice.service.impl;

import com.apexon.compass.entities.Criteria;
import com.apexon.compass.entities.Project;
import com.apexon.compass.entities.ProjectWeekly;
import com.apexon.compass.exception.custom.RecordNotFoundException;
import com.apexon.compass.exception.custom.ServiceException;
import com.apexon.compass.psrservice.dto.PeopleStatusWrapperDto;
import com.apexon.compass.psrservice.dto.PeopleStatusDto;
import com.apexon.compass.psrservice.repository.ProjectRepository;
import com.apexon.compass.psrservice.repository.ProjectWeeklyRepository;
import com.apexon.compass.psrservice.service.PeopleStatusTrendService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.apexon.compass.constants.PsrServiceConstants.*;
import static com.apexon.compass.utilities.ArithmeticUtils.getMatcherCount;
import static com.apexon.compass.utilities.DateTimeUtils.*;
import static org.springframework.data.domain.Sort.Direction.ASC;

@Service
@Slf4j
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
public class PeopleStatusTrendServiceImpl implements PeopleStatusTrendService {

    private ProjectWeeklyRepository projectWeeklyRepository;

    private ProjectRepository projectRepository;

    @Override
    public PeopleStatusWrapperDto getPeopleStatus(int year, int quarter) {
        List<Project> projects = projectRepository.findByIsDeletedNullOrIsDeletedFalse();
        List<ObjectId> ids = new ArrayList<>();
        projects.forEach(iscProject -> ids.add(new ObjectId(iscProject.getId())));
        List<PeopleStatusDto> projectList = new ArrayList<>();
        LocalDate startLocale = null;
        LocalDate endLocale = null;
        try {
            startLocale = getStartDateByQuarter(year, quarter);
            endLocale = getEndDateByQuarter(startLocale, year);
        }
        catch (DateTimeException e) {
            throw new ServiceException("Error in getting Startdate of quarter ");
        }
        log.info("startLocale: " + startLocale + " end Locale : " + startLocale);
        List<ProjectWeekly> projectWeeklyList = null;
        try {
            projectWeeklyList = projectWeeklyRepository.findByDateBetweenAndProjectIDIn(
                    convertLocalDateToDate(startLocale, false), convertLocalDateToDate(endLocale, true), ids,
                    Sort.by(ASC, DATE));
        }
        catch (IllegalArgumentException e) {
            throw new ServiceException("Error converting local date to date " + e.getMessage());
        }
        if (projectWeeklyList.isEmpty()) {
            throw new RecordNotFoundException("Couldn't find record for quarter " + quarter);
        }
        projectWeeklyList.forEach(projectWeekly -> {
            Optional<PeopleStatusDto> dataOptional = projectList.stream()
                .filter(peopleStatusDto -> peopleStatusDto.getName()
                    .equals(convertDateToWeek(new Date(projectWeekly.getDate()))))
                .findAny();
            Optional<Criteria> opitonalCriteria = projectWeekly.getCriteria()
                .stream()
                .filter(criteria -> criteria.getParameter().equalsIgnoreCase(PEOPLE))
                .findAny();
            Criteria criteria = opitonalCriteria.isPresent() ? opitonalCriteria.get() : Criteria.builder().build();
            PeopleStatusDto projectStatusDto;
            if (NAINITIAL.equals(criteria.getValue()) || StringUtils.isBlank(criteria.getValue())) {
                criteria.setValue(NA);
            }
            if (dataOptional.isPresent()) {
                projectStatusDto = dataOptional.get();
                projectStatusDto.setRed(projectStatusDto.getRed() + getMatcherCount(RINITIAL, criteria.getValue()));
                projectStatusDto.setGreen(projectStatusDto.getGreen() + getMatcherCount(GINITIAL, criteria.getValue()));
                projectStatusDto.setAmber(projectStatusDto.getAmber() + getMatcherCount(AINITIAL, criteria.getValue()));
                projectStatusDto.setNa(projectStatusDto.getNa() + getMatcherCount(NA, criteria.getValue()));
            }
            else {
                projectList.add(PeopleStatusDto.builder()
                    .name(convertDateToWeek(new Date(projectWeekly.getDate())))
                    .green(getMatcherCount(GINITIAL, criteria.getValue()))
                    .amber(getMatcherCount(AINITIAL, criteria.getValue()))
                    .red(getMatcherCount(RINITIAL, criteria.getValue()))
                    .na(getMatcherCount(NA, criteria.getValue()))
                    .build());
            }
        });
        projectList.forEach(peopleStatusDto -> {
            int weeklyData = peopleStatusDto.getRed() + peopleStatusDto.getGreen() + peopleStatusDto.getAmber()
                    + peopleStatusDto.getNa();
            if (weeklyData < projects.size()) {
                peopleStatusDto.setNa(peopleStatusDto.getNa() + (projects.size() - weeklyData));
            }
        });
        return PeopleStatusWrapperDto.builder().peopleStatusTrend(projectList).build();
    }

}
