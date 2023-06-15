package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SprintWithBugsVo {

    private ObjectId projectId;

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

    private List<StoryBugsVo> bugs;

}
