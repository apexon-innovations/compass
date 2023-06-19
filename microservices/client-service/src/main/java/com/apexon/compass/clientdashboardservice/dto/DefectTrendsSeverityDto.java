package com.apexon.compass.clientdashboardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DefectTrendsSeverityDto {

    private String id;

    private String name;

    private Integer jiraProjectId;

    private Integer total;

    private PlannedDefectSeverityDto planned;

    private ClosedDefectSeverityDto closed;

    private BacklogDefectSeverityDto backlog;

}
