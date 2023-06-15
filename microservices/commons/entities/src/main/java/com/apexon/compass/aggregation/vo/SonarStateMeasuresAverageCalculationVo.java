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
public class SonarStateMeasuresAverageCalculationVo {

    @Field(value = ID)
    private ObjectId id;

    private double security;

    private double efficiency;

    private double robustness;

}
