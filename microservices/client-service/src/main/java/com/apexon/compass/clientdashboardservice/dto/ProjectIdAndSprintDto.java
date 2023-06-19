package com.apexon.compass.clientdashboardservice.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class ProjectIdAndSprintDto {

    public String projectName;

    public String projectId;

    public Integer jiraProjectId;

    public List<SprintsDto> sprints;

}
