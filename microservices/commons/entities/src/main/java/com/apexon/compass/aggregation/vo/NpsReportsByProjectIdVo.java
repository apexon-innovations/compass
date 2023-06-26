package com.apexon.compass.aggregation.vo;

import static com.apexon.compass.constants.EntitiesConstants.ACCOUNT_MANAGER;
import static com.apexon.compass.constants.EntitiesConstants.BITBUCKET;
import static com.apexon.compass.constants.EntitiesConstants.CLIENT_PROJECT_MANAGER;
import static com.apexon.compass.constants.EntitiesConstants.DELIVERY_MANAGER;
import static com.apexon.compass.constants.EntitiesConstants.PROJECT_MANAGER;
import static com.apexon.compass.constants.EntitiesConstants.SONAR;
import static com.apexon.compass.constants.EntitiesConstants.NPS_SCORE;
import java.util.Date;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import com.apexon.compass.entities.BitBucket;
import com.apexon.compass.entities.IscProjectsNps;
import com.apexon.compass.entities.Jira;
import com.apexon.compass.entities.ManagerDetails;
import com.apexon.compass.entities.NpsScore;
import com.apexon.compass.entities.ProjectManager;
import com.apexon.compass.entities.Resources;
import com.apexon.compass.entities.Sonar;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NpsReportsByProjectIdVo {

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

    private String jiraProjectId;

    private Long lastPsrDashboardDetailsUpdatedDate;

    private boolean isDeleted;

    @Field(name = NPS_SCORE)
    private List<IscProjectsNps> nps_score;

}
