package com.apexon.compass.entities;

import static com.apexon.compass.constants.EntitiesConstants.MONTH;
import static com.apexon.compass.constants.EntitiesConstants.PROJECT_ID;
import static com.apexon.compass.constants.EntitiesConstants.PSR_PROJECT_MONTHLY_DATA;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = PSR_PROJECT_MONTHLY_DATA)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMonthly {

    private ObjectId id;

    @Field(name = PROJECT_ID)
    private ObjectId projectID;

    @Field(name = MONTH)
    private Date monthDate;

    private List<Parameter> metrics;

    @Transient
    private String month;

}
