package com.apexon.compass.entities;

import java.util.Date;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static com.apexon.compass.constants.EntitiesConstants.PSR_PROJECT_ONE_TIME_CRITERIA;
import static com.apexon.compass.constants.EntitiesConstants.PROJECT_ID;
import static com.apexon.compass.constants.EntitiesConstants.LAST_ONE_TIME_PERIODIC_REVIEW_CRITERIA_UPDATED_DATE;
import static com.apexon.compass.constants.EntitiesConstants.LAST_ONE_TIME_PROJECT_START_UP_CRITERIA_UPDATED_DATE;
import static com.apexon.compass.constants.EntitiesConstants.CRITERIA;

import lombok.Builder;
import lombok.Data;

@Document(collection = PSR_PROJECT_ONE_TIME_CRITERIA)
@Data
@Builder
public class ProjectOneTime {

    @Id
    private String id;

    @Field(name = PROJECT_ID)
    private String projectID;

    @Field(name = LAST_ONE_TIME_PERIODIC_REVIEW_CRITERIA_UPDATED_DATE)
    private Date oneTimeCriteriaUpdatedTime;

    @Field(name = LAST_ONE_TIME_PROJECT_START_UP_CRITERIA_UPDATED_DATE)
    private Date oneTimeProjectStartupTime;

    @Field(name = CRITERIA)
    private List<Criteria> criteria;

    private Boolean isArchive;

}
