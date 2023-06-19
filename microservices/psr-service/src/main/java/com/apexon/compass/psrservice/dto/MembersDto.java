package com.apexon.compass.psrservice.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MembersDto {

    private String name;

    private String accountId;

    private MemberStatusDto status;

    private List<TaskDto> tasks;

}
