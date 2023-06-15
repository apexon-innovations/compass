package com.apexon.compass.entities;

import static com.apexon.compass.constants.EntitiesConstants.ALLOCATION;
import static com.apexon.compass.constants.EntitiesConstants.PROJECT_MEMBERS;
import java.util.List;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = PROJECT_MEMBERS)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMembers {

    @Id
    private String id;

    private Integer nestId;

    private String nestProjectId;

    private String memberId;

    private String userName;

    private String email;

    private String name;

    private String initials;

    private String location;

    private String designation;

    private String dp;

    @Field(name = ALLOCATION)
    private List<Allocation> allocation;

    private List<String> leaves;

    @CreatedDate
    private Long createdDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private Long updatedDate;

    @LastModifiedBy
    private String updatedBy;

}