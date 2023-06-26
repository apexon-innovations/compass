package com.apexon.compass.aggregation.vo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectWiseStorySummaryVo {

    private Integer jiraProjectId;

    private Integer boardId;

    private Integer sprintId;

    private String name;

    private Long startDate;

    private Long endDate;

    private String state;

    private Long jiraChangeDate;

    private Boolean isDeleted;

    private Long createdDate;

    private List<StoryStatusVo> stories;

}
