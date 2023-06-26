package com.apexon.compass.psrservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentsDto {

    private String comment;

    private String commentedBy;

    private Long commentedDate;

}
