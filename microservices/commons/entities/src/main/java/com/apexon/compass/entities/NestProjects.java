package com.apexon.compass.entities;

import static com.apexon.compass.constants.EntitiesConstants.ACCOUNT_MANAGER;
import static com.apexon.compass.constants.EntitiesConstants.DELIVERY_DIRECTOR;
import static com.apexon.compass.constants.EntitiesConstants.DELIVERY_MANAGER;
import static com.apexon.compass.constants.EntitiesConstants.NEST_PROJECTS;
import static com.apexon.compass.constants.EntitiesConstants.PROJECT_MANAGER;

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

@Document(collection = NEST_PROJECTS)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NestProjects {

    @Id
    private String id;

    private Integer nestId;

    private String name;

    private String clientName;

    private String iscProjectId;

    private String type;

    private Long startDate;

    private Long endDate;

    private String businessUnit;

    private String industry;

    private String currency;

    private String revenueGeography;

    private String sowNumber;

    @Field(name = PROJECT_MANAGER)
    private List<ManagerDetails> projectManager;

    @Field(name = DELIVERY_MANAGER)
    private List<ManagerDetails> deliveryManager;

    @Field(name = ACCOUNT_MANAGER)
    private List<ManagerDetails> accountManager;

    @Field(name = DELIVERY_DIRECTOR)
    private List<ManagerDetails> deliveryDirector;

    private Long lastSyncStartTime;

    private Long lastSyncEndTime;

    @CreatedDate
    private Long createdDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private Long updatedDate;

    @LastModifiedBy
    private String updatedBy;

}
