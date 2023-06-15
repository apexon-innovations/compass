package com.apexon.compass.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.apexon.compass.constants.EntitiesConstants.JIRA_BOARDS;

@Document(collection = JIRA_BOARDS)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Boards {

    @Id
    private String id;

    private Integer jiraProjectId;

    private Integer boardId;

    private String name;

    private Long jiraUpdatedDate;

    private String state;

    private String isDeleted;

    private Long createdDate;

}
