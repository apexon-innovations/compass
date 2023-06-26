package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlockerVo {

    @Id
    private ObjectId id;

    private Integer jiraProjectId;

    private Integer boardId;

    private ObjectId projectId;

    private Integer sprintId;

    private Integer epicId;

    private Integer storyId;

    private String number;

    private String name;

    private String status;

    private String state;

    private Integer estimate;

    private Integer estimateTime;

    private String url;

    private Long jiraUpdatedDate;

    private Long jiraCreatedDate;

    private Boolean isDeleted;

    private String type;

    private Integer typeId;

}
