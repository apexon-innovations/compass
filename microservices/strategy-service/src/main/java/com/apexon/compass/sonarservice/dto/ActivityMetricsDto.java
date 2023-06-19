package com.apexon.compass.sonarservice.dto;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;
import static com.apexon.compass.constants.PsrServiceConstants.DATA;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonTypeName(DATA)
@JsonTypeInfo(use = NAME, include = WRAPPER_OBJECT)
public class ActivityMetricsDto {

    private List<MonthWiseDto> months;

}
