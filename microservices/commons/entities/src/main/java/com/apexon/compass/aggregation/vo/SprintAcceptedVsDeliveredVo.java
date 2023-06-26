package com.apexon.compass.aggregation.vo;

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
public class SprintAcceptedVsDeliveredVo {

    private ObjectId projectId;

    private Integer jiraProjectId;

    private Integer sprintId;

    private Integer boardId;

    private String name;

    @Field("stories")
    private List<StoriesVo> storiesVos;

}
