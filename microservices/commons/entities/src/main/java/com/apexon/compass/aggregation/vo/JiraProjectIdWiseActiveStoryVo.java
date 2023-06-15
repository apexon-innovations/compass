package com.apexon.compass.aggregation.vo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JiraProjectIdWiseActiveStoryVo {

    private Integer projectId;

    private Integer boardId;

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

    private Boolean isDeleted;

    private String type;

    private Integer typeId;

    private List<String> ownerFullNames;

    @CreatedDate
    private Long createdDate;

}
