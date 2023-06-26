package com.apexon.compass.psrservice.dto;

import static com.apexon.compass.constants.PsrServiceConstants.DATA;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonTypeName(DATA)
@JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
public class SayDoListDto {

    private List<SayDoDto> sprints;

}
