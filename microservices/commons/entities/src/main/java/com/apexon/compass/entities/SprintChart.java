package com.apexon.compass.entities;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import static com.apexon.compass.constants.EntitiesConstants.DERIVED_SPRINTS;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = DERIVED_SPRINTS)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SprintChart {

    private Integer jiraProjectId;

    private Integer boardId;

    private ObjectId projectId;

    private Integer sprintId;

    private String name;

    private Long startDate;

    private Long endDate;

    private String state;

    private Long createdDate;

    private Integer totalEfforts;

    private Integer totalTasks;

    private List<StatusData> statusData;

}
