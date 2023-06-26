package com.apexon.compass.entities;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static com.apexon.compass.constants.EntitiesConstants.SONAR_STATS;
import static com.apexon.compass.constants.EntitiesConstants.SONAR_URL;

import lombok.Builder;
import lombok.Data;

@Document(collection = SONAR_STATS)
@Data
@Builder
public class SonarStats {

    private String sonarProjectId;

    private ObjectId projectId;

    private String name;

    @Field(name = SONAR_URL)
    private String sonarurl;

    private Long sonarUpdatedDate;

    private String securityEfforts;

    private String newCoverage;

    private Integer technicalDebtIndex;

    private String uncoveredLines;

    private String newUncoveredLines;

    private String complexity;

    private String congnitiveComplexity;

    private Duplication duplication;

    private Sqale sqale;

    private Violations violations;

    private NewViolations newViolations;

    private CodeMatrix codeMatrix;

    private QualityMatrix qualityMatrix;

    private Issues issues;

    private Reliability reliability;

    private Ratings ratings;

    private Long createdDate;

}
