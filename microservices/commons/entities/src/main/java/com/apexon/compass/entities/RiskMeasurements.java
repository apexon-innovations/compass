package com.apexon.compass.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import com.apexon.compass.aggregation.vo.ViolationsVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskMeasurements {

    @Id
    private ObjectId id;

    private Long createdDate;

    private String sonarProjectId;

    private ViolationsVO violations;

}
