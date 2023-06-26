package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoryPointsDeliveryVo {

    @Id
    private Long id;

    private Integer jiraProjectId;

    private ObjectId projectId;

    private Integer sprintId;

    private String name;

    private Long startDate;

    private Long endDate;

    private Integer totalEfforts;

    private Integer totalTasks;

    private Integer openTillNowPoints;

    private Integer completedPoints;

    private Integer newlyAddedPoints;

    private Integer reopenPoints;

    private Integer openTillNowCounts;

    private Integer completedCounts;

    private Integer newlyAddedCounts;

    private Integer reopenCounts;

}
