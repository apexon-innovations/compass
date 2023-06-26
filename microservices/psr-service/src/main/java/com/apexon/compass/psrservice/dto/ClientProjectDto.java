package com.apexon.compass.psrservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClientProjectDto {

    private String id;

    private String name;

    private String initials;

    private String category;

    private String iconLocation;

    private String jiraProjectId;

    private List<BoardDetailsDto> boards;

    private List<RepositoryDataDto> repos;

}
