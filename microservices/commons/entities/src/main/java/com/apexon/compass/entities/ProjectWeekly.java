package com.apexon.compass.entities;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import static com.apexon.compass.constants.EntitiesConstants.PSR_PROJECT_WEEKLY_DATA;
import static com.apexon.compass.constants.EntitiesConstants.CRITERIA;
import static com.apexon.compass.constants.EntitiesConstants.PROJECT_ID;
import static com.apexon.compass.constants.EntitiesConstants.OVERALL_HEALTH;
import static com.apexon.compass.constants.EntitiesConstants.DATE;
import static com.apexon.compass.constants.EntitiesConstants.OVERALL_STATUS_UPDATE_DATE;
import lombok.Builder;
import lombok.Data;

@Document(collection = PSR_PROJECT_WEEKLY_DATA)
@Data
@Builder
public class ProjectWeekly {

    @Id
    private String id;

    @Field(name = PROJECT_ID)
    private String projectID;

    @Field(name = OVERALL_HEALTH)
    private String overallHealth;

    @Field(name = CRITERIA)
    private List<Criteria> criteria;

    @Field(name = DATE)
    private Long date;

    @Field(name = OVERALL_STATUS_UPDATE_DATE)
    private Long overallStatusUpdateDate;

}
