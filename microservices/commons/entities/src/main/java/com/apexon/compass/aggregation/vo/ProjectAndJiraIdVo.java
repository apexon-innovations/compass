package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectAndJiraIdVo {

    private ObjectId projectId;

    private Integer jiraProjectId;

    private Integer sprintId;

}
