package com.apexon.compass.psrservice.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class StrategicDetailsDto {

    private OverallProjectHealthDto overallHealth;

    private List<ManagerDto> accountManager;

    private List<NetpromoterDto> npsScore;

    private List<ManagerDto> deliveryManager;

    private List<WeeklyCriteriaDto> weeklyCriteria;

}
