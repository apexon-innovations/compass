package com.apexon.compass.psrservice.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OverallHealthDto {

    private String projectID;

    private String jiraProjectID;

    private String projectName;

    private String projectNameInitials;

    private boolean useImage;

    private String projectCategory;

    private String projectType;

    private String projectStartDate;

    private String projectEndDate;

    private String projectManager;

    private String projectManagerEmail;

    private String deliveryManager;

    private String deliveryManagerEmail;

    private String deliveryDirector;

    private String overallHealth;

    private String projectLogo;

    private Integer pmBandwidth;

    private double actualTeamSize;

    private Map<String, List<Map<String, Object>>> graphData;

    private Map<String, Object> consolidatedMetrics;

    private Long lastUpdatedTime;

    private Map<String, String[]> weekly;

    @JsonIgnore
    private long startDate;

    @JsonIgnore
    private long endDate;

}
