package com.apexon.compass.aggregation.vo;

import com.apexon.compass.entities.Project;
import com.apexon.compass.entities.Sprints;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectsWithJiraConfigurationVo {

    @Id
    private String id;

    private String name;

    private Integer jiraId;

    private List<Sprints> sprints;

    private Project project;

}
