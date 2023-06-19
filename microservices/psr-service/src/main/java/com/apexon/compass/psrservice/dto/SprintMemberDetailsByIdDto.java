package com.apexon.compass.psrservice.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.apexon.compass.constants.PsrServiceConstants.DATA;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonTypeName(DATA)
@JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
@JsonInclude(NON_NULL)
public class SprintMemberDetailsByIdDto {

    private String name;

    private String accountId;

    private String email;

    private String dp;

    private String designation;

    private List<LeavesDto> leaves;

    private Allocation allocation;

}