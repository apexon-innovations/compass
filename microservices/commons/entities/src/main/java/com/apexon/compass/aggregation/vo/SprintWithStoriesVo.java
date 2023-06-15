package com.apexon.compass.aggregation.vo;

import static com.apexon.compass.constants.EntitiesConstants.SPRINT_TICKETS;

import com.apexon.compass.entities.Stories;
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
public class SprintWithStoriesVo {

    private ObjectId projectId;

    private Integer jiraProjectId;

    private Integer boardId;

    private Integer sprintId;

    private String name;

    private Long startDate;

    private Long endDate;

    private String state;

    private Long jiraChangeDate;

    private Boolean isDeleted;

    private Long createdDate;

    @Field(SPRINT_TICKETS)
    private List<Stories> sprintTickets;

}
