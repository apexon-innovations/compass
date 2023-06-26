package com.apexon.compass.psrservice.service.impl;

import static com.apexon.compass.constants.PsrServiceConstants.ACTIVE;
import static com.apexon.compass.constants.PsrServiceConstants.BACKLOG;
import static com.apexon.compass.constants.PsrServiceConstants.BLOCKER;
import static com.apexon.compass.constants.PsrServiceConstants.CLOSED;
import static com.apexon.compass.constants.PsrServiceConstants.COMPLETED;
import static com.apexon.compass.constants.PsrServiceConstants.DONE;
import static com.apexon.compass.constants.PsrServiceConstants.FLAGGED;
import static com.apexon.compass.constants.PsrServiceConstants.IN_PROGRESS;
import static com.apexon.compass.constants.PsrServiceConstants.OPEN;
import static com.apexon.compass.constants.PsrServiceConstants.SCOPE_CHANGE;
import static com.apexon.compass.constants.PsrServiceConstants.TIME_ELAPSED;
import static com.apexon.compass.constants.PsrServiceConstants.TODO;
import static com.apexon.compass.constants.PsrServiceConstants.TO_DO;
import static com.apexon.compass.constants.PsrServiceConstants.WORK_COMPLETED;
import static com.apexon.compass.utilities.ArithmeticUtils.getRatioOfInteger;
import static com.apexon.compass.utilities.DateTimeUtils.getDaysFromDates;
import static com.apexon.compass.utilities.DateTimeUtils.getTimeRatio;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.apexon.compass.psrservice.dto.FeatureStagesDto;
import com.apexon.compass.psrservice.dto.SprintProgressDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.apexon.compass.entities.SprintChart;
import com.apexon.compass.entities.StatusData;
import com.apexon.compass.entities.Stories;
import com.apexon.compass.exception.custom.RecordNotFoundException;
import com.apexon.compass.exception.custom.ServiceException;
import com.apexon.compass.exception.custom.TypeConversionException;
import com.apexon.compass.psrservice.dto.DeliveredAcceptedDto;
import com.apexon.compass.psrservice.dto.FeaturesDto;
import com.apexon.compass.psrservice.dto.SprintStatusDto;
import com.apexon.compass.psrservice.repository.SprintChartRepository;
import com.apexon.compass.psrservice.repository.StoriesRepository;
import com.apexon.compass.psrservice.service.StoriesService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
public class StoriesServiceImpl implements StoriesService {

    private SprintChartRepository sprintChartRepository;

    private StoriesRepository storiesRepository;

    public SprintProgressDto getCurrentSprintProgress(String jiraProjectId) {
        List<SprintChart> sprintCharts = sprintChartRepository
            .findByJiraProjectIdAndState(Integer.parseInt(jiraProjectId), ACTIVE);
        if (sprintCharts.isEmpty()) {
            throw new RecordNotFoundException("No jira data available for this id ");
        }
        List<StatusData> statusData = sprintCharts.get(0).getStatusData();
        return SprintProgressDto.builder()
            .sprintStatus(getSprintStatus(sprintCharts))
            .featureStatus(getFeatureStatus(statusData.stream().skip(statusData.size() - 1).toList().get(0)))
            .remainingDays(getDaysFromDates(sprintCharts.get(0).getStartDate(), sprintCharts.get(0).getEndDate()))
            .endDate(sprintCharts.get(0).getEndDate())
            .startDate(sprintCharts.get(0).getStartDate())
            .sprintName(sprintCharts.get(0).getName())
            .sprintId(sprintCharts.get(0).getSprintId())
            .lastUpdated(sprintCharts.get(0).getCreatedDate())
            .projectName(StringUtils.EMPTY)
            .jiraProjectId(sprintCharts.get(0).getJiraProjectId())
            .projectId(StringUtils.EMPTY)
            .build();
    }

    public List<FeaturesDto> getFeatureStatus(StatusData lastDayStatusData) {
        List<FeaturesDto> featureStatusList = new ArrayList<>();
        featureStatusList.add(FeaturesDto.builder().status(TO_DO).count(lastDayStatusData.getToDoTasks()).build());
        featureStatusList
            .add(FeaturesDto.builder().status(COMPLETED).count(lastDayStatusData.getInProgressTasks()).build());
        featureStatusList
            .add(FeaturesDto.builder().status(IN_PROGRESS).count(lastDayStatusData.getCompletedTasks()).build());
        return featureStatusList;
    }

    public List<SprintStatusDto> getSprintStatus(List<SprintChart> sprintCharts) {
        List<SprintStatusDto> sprintStatusList = new ArrayList<>();
        List<StatusData> sortedStatusData = sprintCharts.get(0).getStatusData().stream().toList();

        int completedEfforts = sprintCharts.get(0).getTotalEfforts()
                - sortedStatusData.get(sortedStatusData.size() - 1).getRemainingEfforts();

        try {
            sprintStatusList.add(SprintStatusDto.builder()
                .label(TIME_ELAPSED)
                .value(getTimeRatio(sprintCharts.get(0).getStartDate(), sprintCharts.get(0).getEndDate()))
                .build());
        }
        catch (NumberFormatException e) {
            throw new ServiceException("Error in getting Time Ratio");
        }

        try {
            sprintStatusList.add(SprintStatusDto.builder()
                .label(WORK_COMPLETED)
                .value(getRatioOfInteger(completedEfforts, sprintCharts.get(0).getTotalEfforts()).toString())
                .build());
        }
        catch (NumberFormatException e) {
            throw new ServiceException("Error in getting RatioOfInteger");
        }
        sprintStatusList.add(SprintStatusDto.builder().label(SCOPE_CHANGE).value(StringUtils.EMPTY).build());
        sprintStatusList.add(SprintStatusDto.builder().label(FLAGGED).value(StringUtils.EMPTY).build());
        sprintStatusList.add(SprintStatusDto.builder().label(BLOCKER).value(StringUtils.EMPTY).build());
        return sprintStatusList;
    }

    public FeatureStagesDto getFeaturesAtDiffrentStage(String jiraProjectId) {
        List<FeaturesDto> getList = new ArrayList<>();
        List<Stories> storyList = storiesRepository.findByJiraProjectIdAndType(Integer.valueOf(jiraProjectId), "Story");
        if (storyList.isEmpty()) {
            throw new RecordNotFoundException("No record found for: " + jiraProjectId);
        }
        try {
            int backlog = (int) storyList.stream().filter(story -> story.getStatus().equalsIgnoreCase(BACKLOG)).count();
            getList.add(FeaturesDto.builder().status(BACKLOG).count(backlog).build());
            int toDo = (int) storyList.stream().filter(story -> story.getStatus().equalsIgnoreCase(OPEN)).count();
            getList.add(FeaturesDto.builder().status(TODO).count(toDo).build());
            int done = (int) storyList.stream().filter(story -> story.getStatus().equalsIgnoreCase(DONE)).count();
            getList.add(FeaturesDto.builder().status(DONE).count(done).build());
            int inProgress = (int) storyList.stream()
                .filter(story -> story.getStatus().equalsIgnoreCase(IN_PROGRESS))
                .count();
            getList.add(FeaturesDto.builder().status(IN_PROGRESS).count(inProgress).build());
            return FeatureStagesDto.builder()
                .data(getList)
                .lastUpdated(storyList.get(0).getJiraUpdatedDate())
                .projectName(StringUtils.EMPTY)
                .jiraProjectId(jiraProjectId)
                .projectId(StringUtils.EMPTY)
                .build();
        }
        catch (ClassCastException ex) {
            throw new TypeConversionException("Exception in type conversion " + ex.getMessage());
        }
    }

    public DeliveredAcceptedDto getAcceptedDeliveredFeatures(String jiraProjectId) {
        List<Stories> storyList = storiesRepository.findByJiraProjectIdAndType(Integer.valueOf(jiraProjectId), "Story");
        if (storyList.isEmpty()) {
            throw new RecordNotFoundException("No record found for:" + jiraProjectId);
        }
        try {
            Supplier<Stream<Stories>> storiesSupplier = storyList::stream;
            int acceptedStory = (int) storiesSupplier.get()
                .filter(story -> story.getStatus().equalsIgnoreCase(CLOSED))
                .count();
            return DeliveredAcceptedDto.builder()
                .projectId(StringUtils.EMPTY)
                .jiraProjectId(storyList.get(0).getJiraProjectId())
                .projectName(StringUtils.EMPTY)
                .lastUpdated(storyList.get(0).getJiraUpdatedDate())
                .target(storyList.size())
                .completed(storiesSupplier.get().filter(story -> story.getStatus().equalsIgnoreCase(DONE)).count()
                        + acceptedStory)
                .accepted(acceptedStory)
                .build();
        }
        catch (ClassCastException ex) {
            throw new TypeConversionException("Exception in type conversion " + ex.getMessage());
        }
    }

}
