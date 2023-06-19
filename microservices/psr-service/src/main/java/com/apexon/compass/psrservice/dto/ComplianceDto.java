package com.apexon.compass.psrservice.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;
import static com.apexon.compass.constants.PsrServiceConstants.DATA;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonTypeName(DATA)
@JsonTypeInfo(use = NAME, include = WRAPPER_OBJECT)
@JsonInclude(NON_NULL)
public class ComplianceDto {

    private String id;

    private String nestId;

    private String projectId;

    private ComplianceSummaryDto complianceSummary;

    private List<TicketObservationDto> tickets;

    private List<TicketObservationDto> observations;

}
