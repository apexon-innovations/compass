package com.apexon.compass.psrservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class SprintBurndownDataDto {

    private Long date;

    private Integer completedTasks;

    private String name;

    private Integer remainingEfforts;

    private Integer remainingTasks;

    private Integer idealCompleted;

}
