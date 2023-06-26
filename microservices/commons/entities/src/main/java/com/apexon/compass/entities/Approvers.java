package com.apexon.compass.entities;

import static com.apexon.compass.constants.StrategyServiceConstants.ID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Approvers {

    @Field(ID)
    private String id;

    private String name;

    private Long at;

}
