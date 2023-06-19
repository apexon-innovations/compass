package com.apexon.compass.psrservice.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(NON_NULL)
public class SummaryDto {

    private Integer total;

    private Integer todo;

    private Integer inProgress;

    private Integer completed;

    private Integer assigned;

    private Integer unAssigned;

}
