package com.apexon.compass.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewViolations {

    private Integer blocker;

    private Integer major;

    private Integer critical;

    private Integer info;

    private Integer minor;

    private Integer total;

}
