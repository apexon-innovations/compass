package com.apexon.compass.psrservice.service.impl;

import com.apexon.compass.entities.Project;
import com.apexon.compass.entities.ProjectWeekly;
import com.apexon.compass.exception.custom.RecordNotFoundException;
import com.apexon.compass.exception.custom.ServiceException;
import com.apexon.compass.psrservice.dto.WeeklyStatusDto;
import com.apexon.compass.psrservice.dto.WeeklyStatusWrapperDto;
import com.apexon.compass.psrservice.repository.ProjectRepository;
import com.apexon.compass.psrservice.repository.ProjectWeeklyRepository;
import com.apexon.compass.psrservice.service.WeeklyStatusService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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

@Service
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
public class WeeklyStatusServiceImpl implements WeeklyStatusService {

    private ProjectWeeklyRepository projectWeeklyRepository;

    private ProjectRepository projectRepository;

    public WeeklyStatusWrapperDto getWeeklyProjectStatus(int year) {
        List<Project> projects = projectRepository.findByIsDeletedNullOrIsDeletedFalse();
        List<ObjectId> ids = new ArrayList<>();
        projects.forEach(iscProject -> ids.add(new ObjectId(iscProject.getId())));
        List<WeeklyStatusDto> weeklyStatus = new ArrayList<>();
        LocalDate startLocale = null;
        LocalDate endLocale = LocalDate.of(year, 12, 31);
        try {
            startLocale = getStartDateByQuarter(year, 1);
        }
        catch (DateTimeException e) {
            throw new ServiceException("Error in getting Startdate of quarter ");
        }

        List<ProjectWeekly> projectWeeklyList = null;
        try {
            projectWeeklyList = projectWeeklyRepository.findByDateBetweenAndProjectIDIn(
                    convertLocalDateToDate(startLocale, false), convertLocalDateToDate(endLocale, true), ids,
                    Sort.by(Direction.ASC, DATE));
        }
        catch (IllegalArgumentException ex) {
            throw new ServiceException("Error converting local date to date " + ex.getMessage());
        }
        if (projectWeeklyList.isEmpty()) {
            throw new RecordNotFoundException("Couldn't find weekly data for given year");
        }
        projectWeeklyList.forEach(projectWeekly -> {
            String key = convertDateToWeek(new Date(projectWeekly.getDate()));
            Optional<WeeklyStatusDto> dataOptional = weeklyStatus.stream()
                .filter(weeklyStatusDto -> weeklyStatusDto.getName().equals(key))
                .findAny();
            if (NAINITIAL.equals(projectWeekly.getOverallHealth())
                    || StringUtils.isBlank(projectWeekly.getOverallHealth())) {
                projectWeekly.setOverallHealth(NA);
            }
            WeeklyStatusDto weeklyStatusDto;
            if (dataOptional.isPresent()) {
                weeklyStatusDto = dataOptional.get();
                weeklyStatusDto.setOnTrack(
                        weeklyStatusDto.getOnTrack() + getMatcherCount(GINITIAL, projectWeekly.getOverallHealth()));
                weeklyStatusDto
                    .setDelay(weeklyStatusDto.getDelay() + getMatcherCount(AINITIAL, projectWeekly.getOverallHealth()));
                weeklyStatusDto.setCritical(
                        weeklyStatusDto.getCritical() + getMatcherCount(RINITIAL, projectWeekly.getOverallHealth()));
                weeklyStatusDto.setNa(weeklyStatusDto.getNa() + getMatcherCount(NA, projectWeekly.getOverallHealth()));
            }
            else {
                weeklyStatus.add(WeeklyStatusDto.builder()
                    .name(key)
                    .onTrack(getMatcherCount(GINITIAL, projectWeekly.getOverallHealth()))
                    .delay(getMatcherCount(AINITIAL, projectWeekly.getOverallHealth()))
                    .critical(getMatcherCount(RINITIAL, projectWeekly.getOverallHealth()))
                    .na(getMatcherCount(NA, projectWeekly.getOverallHealth()))
                    .formattedDate(convertDateToTimeStamp(new Date(projectWeekly.getDate())))
                    .date(String.valueOf(new Date(projectWeekly.getDate())))
                    .build());
            }
        });
        weeklyStatus.forEach(weeklyStatusDto -> {
            int weeklyData = weeklyStatusDto.getCritical() + weeklyStatusDto.getDelay() + weeklyStatusDto.getOnTrack()
                    + weeklyStatusDto.getNa();
            if (weeklyData < projects.size()) {
                weeklyStatusDto.setNa(weeklyStatusDto.getNa() + (projects.size() - weeklyData));
            }
        });
        return WeeklyStatusWrapperDto.builder().weeklyProjectStatus(weeklyStatus).build();
    }

}
