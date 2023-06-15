package com.apexon.compass.entities;

import static com.apexon.compass.constants.EntitiesConstants.SPRINTS;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = SPRINTS)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sprints {

    private Integer jiraProjectId;

    private Integer boardId;

    private ObjectId projectId;

    private Integer sprintId;

    private String name;

    private Long startDate;

    private Long endDate;

    private String state;

    private Long jiraChangeDate;

    private Boolean isDeleted;

    private Long createdDate;

}
