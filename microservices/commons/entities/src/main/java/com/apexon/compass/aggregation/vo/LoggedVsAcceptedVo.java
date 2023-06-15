package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoggedVsAcceptedVo {

    @Id
    private Double id;

    private Double totalCompleted;

    private Double totalLogged;

}
