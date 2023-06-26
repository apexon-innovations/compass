package com.apexon.compass.entities;

import java.util.List;
import org.bson.types.ObjectId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import static com.apexon.compass.constants.EntitiesConstants.PROJECT_MEMBERS_LEAVES;

@Document(PROJECT_MEMBERS_LEAVES)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMemberLeaves {

    @Id
    private ObjectId id;

    private ObjectId nestProjectId;

    private String memberId;

    private String name;

    private List<Leaves> leaves;

}
