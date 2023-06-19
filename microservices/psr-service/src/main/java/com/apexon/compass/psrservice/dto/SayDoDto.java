package com.apexon.compass.psrservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SayDoDto {

    private Integer id;

    private String name;

    private String jiraProjectId;

    private Integer boardId;

    private Integer totalExpected;

    private Integer totalCompleted;

}
