package com.apexon.compass.psrservice.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(NON_NULL)
public class DailyAcceptedDeliveredDto {

    private String jiraProjectId;

    private List<TasksDto> data;

    private String projectName;

    private String projectId;

}
