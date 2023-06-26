package com.apexon.compass.aggregation.vo;

import static com.apexon.compass.constants.EntitiesConstants.ACCOUNT_MANAGER;
import static com.apexon.compass.constants.EntitiesConstants.DELIVERY_DIRECTOR;
import static com.apexon.compass.constants.EntitiesConstants.DELIVERY_MANAGER;
import static com.apexon.compass.constants.EntitiesConstants.PROJECT_MANAGER;
import static com.apexon.compass.constants.EntitiesConstants.PROJECT_MEMBERS;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NestProjectWiseProjectMembersVo {

    private String nestId;

    private ObjectId iscProjectId;

    private String name;

    private String clientName;

    @Field(name = ACCOUNT_MANAGER)
    private List<ManageresVo> accountManagersVo;

    @Field(name = PROJECT_MANAGER)
    private List<ManageresVo> projectManagersVo;

    @Field(name = DELIVERY_MANAGER)
    private List<ManageresVo> deliveryManagersVo;

    @Field(name = DELIVERY_DIRECTOR)
    private List<ManageresVo> deliveryDirectorsVo;

    @Field(name = PROJECT_MEMBERS)
    private List<ProjectMembersDetailsVo> projectMembersDetailsVo;

}
