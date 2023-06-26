package com.apexon.compass.entities;

import static com.apexon.compass.constants.EntitiesConstants.JIRA_CONFIGURATION;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = JIRA_CONFIGURATION)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JiraConfiguration {

    @Id
    private String id;

    private ObjectId projectId;

    private String jiraProjectId;

    private List<String> jiraBoardId;

    private String url;

    private String queryEndPoint;

    private Long projectStartDate;

    private String jiraApiKey;

    private List<String> issueTypes;

    private String sprintDataFieldName;

    private String jiraEpicIdFieldName;

    private String jiraStoryPointsFieldName;

    private boolean jiraBoardAsTeam;

    private Map<String, String> issueTypesId = new HashMap<>();

    private String cron;

    private String createdBy;

    private Long createdDate;

    private String updatedBy;

    private Long updatedDate;

    // Added Extra Default Fields
    private int maxNumberOfFeaturesPerBoard = 10000;

    private String jiraTeamFieldName = "";

    private int firstRunHistoryDays = 7200;

    private String jiraProxyUrl = "";

    private String jiraProxyPort = "";

    private int refreshTeamAndProjectHours = 3;

    private boolean collectorItemOnlyUpdate = false;

}
