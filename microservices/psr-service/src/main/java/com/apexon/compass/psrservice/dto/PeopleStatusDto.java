package com.apexon.compass.psrservice.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.apexon.compass.constants.PsrServiceConstants.AMBERCAP;
import static com.apexon.compass.constants.PsrServiceConstants.GREENCAP;
import static com.apexon.compass.constants.PsrServiceConstants.NA;
import static com.apexon.compass.constants.PsrServiceConstants.NAME;
import static com.apexon.compass.constants.PsrServiceConstants.REDCAP;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class PeopleStatusDto {

    @JsonProperty(REDCAP)
    public Integer red;

    @JsonProperty(AMBERCAP)
    public Integer amber;

    @JsonProperty(NAME)
    public String name;

    @JsonProperty(GREENCAP)
    public Integer green;

    @JsonProperty(NA)
    public Integer na;

}
