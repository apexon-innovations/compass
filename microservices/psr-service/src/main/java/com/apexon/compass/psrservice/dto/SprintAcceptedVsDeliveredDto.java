package com.apexon.compass.psrservice.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(NON_NULL)
public class SprintAcceptedVsDeliveredDto {

    private String id;

    private String jiraProjectId;

    private String sprintId;

    private String sprintName;

    private Integer boardId;

    private AcceptedVsDeliveredSummaryDto summary;

}
