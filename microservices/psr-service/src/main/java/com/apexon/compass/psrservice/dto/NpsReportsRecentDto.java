package com.apexon.compass.psrservice.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.apexon.compass.constants.PsrServiceConstants.DATA;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@JsonTypeName(DATA)
@JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
@JsonInclude(NON_NULL)
public class NpsReportsRecentDto extends NpsReportsDto {

    public NpsReportsRecentDto(String iscProjectId, double averageNps, RecentSurveyDto survey) {
        super(iscProjectId, averageNps);
        this.survey = survey;
    }

    private RecentSurveyDto survey;

}
