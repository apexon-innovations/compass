package com.apexon.compass.aggregation.vo;

import static com.apexon.compass.constants.PsrServiceConstants.ID;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMembersDetailsVo {

    @Field(name = ID)
    private String id;

    private Integer nestId;

    private String nestProjectId;

    private String memberId;

    private String userName;

    private String email;

    private String name;

    private String location;

    private String designation;

    private String dp;

    private List<AllocationVo> allocation;

}
