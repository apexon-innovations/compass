package com.apexon.compass.psrservice.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.apexon.compass.constants.PsrServiceConstants.CRITICAL;
import static com.apexon.compass.constants.PsrServiceConstants.DATE_CAPITALIZED;
import static com.apexon.compass.constants.PsrServiceConstants.DELAY;
import static com.apexon.compass.constants.PsrServiceConstants.FORMATTED_DATE;
import static com.apexon.compass.constants.PsrServiceConstants.NA;
import static com.apexon.compass.constants.PsrServiceConstants.NAME;
import static com.apexon.compass.constants.PsrServiceConstants.ON_TRACK;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(NON_NULL)
public class WeeklyStatusDto {

    @JsonProperty(ON_TRACK)
    public Integer onTrack;

    @JsonProperty(FORMATTED_DATE)
    public String formattedDate;

    @JsonProperty(NAME)
    public String name;

    @JsonProperty(CRITICAL)
    public Integer critical;

    @JsonProperty(DELAY)
    public Integer delay;

    @JsonProperty(NA)
    public Integer na;

    @JsonProperty(DATE_CAPITALIZED)
    public String date;

}
