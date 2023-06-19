package com.apexon.compass.psrservice.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectDto {

    private String id;

    private String name;

    private String initials;

    private String category;

    private String iconLocation;

    private String overallHealth;

    private String jiraProjectId;

    private List<BoardDetailsDto> boards;

    private List<RepositoryDataDto> repos;

}
