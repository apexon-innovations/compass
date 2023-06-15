package com.apexon.compass.aggregation.vo;

import static com.apexon.compass.constants.EntitiesConstants.ACCOUNT_MANAGER;
import static com.apexon.compass.constants.EntitiesConstants.DELIVERY_DIRECTOR;
import static com.apexon.compass.constants.EntitiesConstants.DELIVERY_MANAGER;
import static com.apexon.compass.constants.EntitiesConstants.PROJECT_MANAGER;
import static com.apexon.compass.constants.EntitiesConstants.PROJECT_MEMBERS;
import static com.apexon.compass.constants.EntitiesConstants.PROJECT_MEMBERS_LEAVES;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import com.apexon.compass.entities.ManagerDetails;
import com.apexon.compass.entities.ProjectMemberLeaves;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NestMemberDetailsByMemberIdVo {

    @Id
    private String id;

    private Integer nestId;

    private String name;

    private String clientName;

    private ObjectId iscProjectId;

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
    private List<ManagerDetails> deliverManager;

    @Field(name = ACCOUNT_MANAGER)
    private List<ManagerDetails> accountManager;

    @Field(name = DELIVERY_DIRECTOR)
    private List<ManagerDetails> deliverDirector;

    private Long lastSyncStartTime;

    private Long lastSyncEndTime;

    @Field(name = PROJECT_MEMBERS)
    private List<ProjectMembersDetailsVo> projectMembers;

    @Field(name = PROJECT_MEMBERS_LEAVES)
    private List<ProjectMemberLeaves> projectMemberLeaves;

}
