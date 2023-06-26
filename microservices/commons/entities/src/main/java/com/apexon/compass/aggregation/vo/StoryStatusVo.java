package com.apexon.compass.aggregation.vo;

import static com.apexon.compass.constants.PsrServiceConstants.ID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoryStatusVo {

    @Field(ID)
    private Integer id;

    private Integer total;

    private Integer totalCompleted;

    private Integer totalToDo;

    private Integer totalInProgress;

    private Integer totalAssigned;

    private Integer totalUnassigned;

}
