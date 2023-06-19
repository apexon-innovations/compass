package com.apexon.compass.psrservice.service;

import com.apexon.compass.psrservice.dto.MemberStatusDto;
import com.apexon.compass.psrservice.dto.MembersDto;
import com.apexon.compass.psrservice.dto.SprintMemberStatusByIdDto;
import com.apexon.compass.psrservice.dto.SprintMemberStatusDto;

public class JiraServiceImplUtils {

    public static SprintMemberStatusDto getSprintMemberStatusDto() {
        return SprintMemberStatusDto.builder()
            .id("5e7dbe36215e091c132c8f88")
            .jiraProjectId("10003")
            .sprintId("11")
            .sprintName("IA Sprint 9")
            .build();
    }

    public static SprintMemberStatusByIdDto getSprintMemberStatusByIdDtoDto() {
        return SprintMemberStatusByIdDto.builder()
            .id("5e7dbe36215e091c132c8f88")
            .jiraProjectId("10003")
            .sprintId("11")
            .sprintName("IA Sprint 9")
            .member(MembersDto.builder()
                .name("Mitul Vora")
                .accountId("5d1df9bbe3f1b90d1765f307")
                .status(MemberStatusDto.builder().todo(1).build())
                .build())
            .build();
    }

}
