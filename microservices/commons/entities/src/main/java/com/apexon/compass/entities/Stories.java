package com.apexon.compass.entities;

import org.springframework.data.mongodb.core.mapping.Document;
import static com.apexon.compass.constants.EntitiesConstants.STORIES;
import lombok.Builder;
import lombok.Data;

@Document(collection = STORIES)
@Data
@Builder
public class Stories {

    private String id;

    private Integer jiraProjectId;

    private Integer epicId;

    private Integer sprintId;

    private Integer boardId;

    private Integer storyId;

    private String number;

    private String name;

    private String status;

    private String state;

    private Integer estimate;

    private Integer estimatedTime;

    private String url;

    private Long jiraUpdatedDate;

    private Boolean isDeleted;

    private String type;

    private Integer typeId;

    private Long createdDate;

}
