package com.apexon.compass.psrservice.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PeopleStatusWrapperDto {

    private List<PeopleStatusDto> peopleStatusTrend;

}
