package com.apexon.compass.aggregation.vo;

import com.apexon.compass.entities.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

import static com.apexon.compass.constants.EntitiesConstants.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllActiveClientProjectVo {

    @Id
    private String id;

    private String name;

    private String clientName;

    private String category;

    private String psrLocation;

    private String nestId;

    private List<Jira> jira;

    private String billingType;

    private Date startDate;

    private Date endDate;

    private Date projectedEndDate;

    private String fileName;

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

    private String useImage;

    private String jiraProjectId;

    private Long lastPsrDashboardDetailsUpdatedDate;

    private List<Repos> repos;

    @CreatedDate
    private Long createdDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private Long updatedDate;

    @LastModifiedBy
    private String updatedBy;

    private boolean isDeleted;

}
