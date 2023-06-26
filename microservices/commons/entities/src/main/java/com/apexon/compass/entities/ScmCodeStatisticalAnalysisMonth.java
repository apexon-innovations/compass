package com.apexon.compass.entities;

import static com.apexon.compass.constants.EntitiesConstants.SCM_CODE_STATISTICAL_ANALYSIS_MONTH;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = SCM_CODE_STATISTICAL_ANALYSIS_MONTH)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScmCodeStatisticalAnalysisMonth {

    private @Id ObjectId id;

    private Long date;

    private ObjectId projectId;

    private ObjectId scmConfigurationId;

    private String repoName;

    private String repoUrl;

    private Integer jiraProjectId;

    private Integer addedLineOfCode;

    private Integer removedLineOfCode;

    private Integer totalLineOfCode;

    private Long createdDate;

}
