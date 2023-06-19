package com.apexon.compass.psrservice.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(NON_NULL)
public class TicketObservationDto {

    private String status;

    private Long id;

    private String number;

    private String name;

    private String type;

    private String url;

    private String category;

    private Long ncRaisedDate;

    private Long ncDueDate;

}
