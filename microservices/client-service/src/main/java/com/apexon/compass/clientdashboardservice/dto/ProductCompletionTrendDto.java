package com.apexon.compass.clientdashboardservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
public class ProductCompletionTrendDto {

    private String id;

    private String name;

    private Integer jiraProjectId;

    private String projectId;

    private List<ProductCompletionSprintDetails> sprints;

}
