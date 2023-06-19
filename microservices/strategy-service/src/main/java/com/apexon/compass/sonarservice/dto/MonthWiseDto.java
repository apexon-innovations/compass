package com.apexon.compass.sonarservice.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MonthWiseDto {

    private String dateRange;

    private List<MemberDto> members;

}
