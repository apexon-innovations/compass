package com.apexon.compass.psrservice.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(NON_NULL)
public class DeliveredAcceptedDto {

    private Long lastUpdated;

    private Integer jiraProjectId;

    private Integer accepted;

    private Long completed;

    private String projectName;

    private String projectId;

    private Integer target;

}
