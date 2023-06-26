package com.apexon.compass.aggregation.vo;

import static com.apexon.compass.constants.PsrServiceConstants.ID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TechnicalDebtVo {

    @Field(value = ID)
    private ObjectId id;

    private Integer complexity;

    private Double duplication;

    private Integer issues;

    private Integer maintainability;

    private Integer reliability;

    private Double security;

    private Integer size;

    private Double tests;

}
