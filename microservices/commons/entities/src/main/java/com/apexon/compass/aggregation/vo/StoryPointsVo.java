package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoryPointsVo {

    private IdVo _id;

    private Integer jiraProjectId;

    private String projectId;

    private Integer sprintId;

    private String name;

    private Long startDate;

    private Long endDate;

    private Integer totalEfforts;

    private Integer totalTasks;

    private List<StatusDataVo> statusData;

}
