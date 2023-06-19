package com.apexon.compass.psrservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OverallProjectHealthWrapperDto {

    private OverallProjectHealthDto overAllHealth;

}
