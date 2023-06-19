package com.apexon.compass.psrservice.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.apexon.compass.constants.PsrServiceConstants.CRITICAL;
import static com.apexon.compass.constants.PsrServiceConstants.DELAY;
import static com.apexon.compass.constants.PsrServiceConstants.NAME;
import static com.apexon.compass.constants.PsrServiceConstants.ON_TRACK;
import static com.apexon.compass.constants.PsrServiceConstants.NA;
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
public class ManagerDto {

    @JsonProperty(ON_TRACK)
    public Integer onTrack;

    @JsonProperty(NAME)
    public String name;

    @JsonProperty(CRITICAL)
    public Integer critical;

    @JsonProperty(DELAY)
    public Integer delay;

    @JsonProperty(NA)
    public Integer na;

}
