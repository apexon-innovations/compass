package com.apexon.compass.aggregation.vo;

import static com.apexon.compass.constants.EntitiesConstants.STORIES;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = STORIES)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoriesMemberStatusVo {

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

    private Integer estimatedTime;

    private String url;

    private Long jiraUpdatedDate;

    private Long jiraCreatedDate;

    private Boolean isDeleted;

    private String type;

    private Integer typeId;

    private List<String> ownerFullNames;

    private List<String> ownerIds;

    private List<String> sprintJourney;

    @CreatedDate
    private Long createdDate;

}
