package com.apexon.compass.aggregation.vo;

import com.apexon.compass.entities.JiraConfiguration;
import com.apexon.compass.entities.Repos;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IscProjectsWithJiraConfigurationDataVo {

    @Id
    private String id;

    private String name;

    private String fileName;

    private List<Repos> repos;

    private List<JiraConfiguration> jiraData;

}
