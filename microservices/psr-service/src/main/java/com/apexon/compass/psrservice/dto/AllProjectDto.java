package com.apexon.compass.psrservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AllProjectDto {

    private List<ProjectDataDto> data;

}
