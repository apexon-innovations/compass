package com.apexon.compass.aggregation.vo;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RiskMeasurementsVo {

    @Id
    private ObjectId id;

    private List<ProjectRiskMeasurementVo> Project;

}
