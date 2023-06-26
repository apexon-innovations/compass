package com.apexon.compass.entities;

import static com.apexon.compass.constants.EntitiesConstants.*;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = PROJECTS)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    @Id
    private String id;

    private String name;

    private String clientName;

    private String category;

    private String psrLocation;

    private String nestId;

    private List<String> nestIds;

    private List<Jira> jira;

    private String billingType;

    private long startDate;

    private long endDate;

    private Date projectedEndDate;

    @Field(name = PROJECT_MANAGER)
    private List<ProjectManager> projectManager;

    @Field(name = DELIVERY_MANAGER)
    private List<ManagerDetails> deliveryManager;

    @Field(name = ACCOUNT_MANAGER)
    private List<ManagerDetails> accountManager;

    @Field(name = CLIENT_PROJECT_MANAGER)
    private List<ManagerDetails> clientProjectManager;

    private List<Resources> resources;

    @Field(name = BITBUCKET)
    private List<BitBucket> bitbucket;

    @Field(name = SONAR)
    private List<Sonar> sonar;

    private List<NpsScore> npsScore;

    private String solutionTypeDistribution;

    private String industrySpecificExposure;

    private String deliveryDirector;

    private String projectLogo;

    private String logoName;

    private String useImage;

    private String jiraProjectId;

    private Long lastPsrDashboardDetailsUpdatedDate;

    @CreatedDate
    private Long createdDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private Long updatedDate;

    @LastModifiedBy
    private String updatedBy;

    private boolean isDeleted;

    private String logoUrl;

    private String fileName;

    private List<Repos> repos;

}
