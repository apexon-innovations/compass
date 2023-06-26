package com.apexon.compass.aggregation.vo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JiraConfigurationWiseCurrentSprintVo {

    private ObjectId projectId;

    private String jiraProjectId;

    private List<String> jiraBoardId;

    private String url;

    private String queryEndPoint;

    private String projectStartDate;

    private String jiraApiKey;

    private List<String> issueTypes;

    private String sprintDataFieldName;

    private String jiraEpicIdFieldName;

    private String jiraStoryPointsFieldName;

    private Boolean jiraBoardAsTeam;

    private String cron;

    private String createdBy;

    private String createdDate;

    private String updatedBy;

    private String updatedDate;

    private List<CurrentSprintVo> current_sprint;

    private List<BoardsVo> boards;

}
