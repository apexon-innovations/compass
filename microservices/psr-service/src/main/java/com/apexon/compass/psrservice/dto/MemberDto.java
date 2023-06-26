package com.apexon.compass.psrservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberDto {

    private String name;

    private String accountId;

    private String memberId;

    private Integer todo;

    private Integer inProgress;

    private Integer completed;

    private Integer availableHours;

}
