package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodeSnapshotViolationsVo {

    private ObjectId _id;

    private Integer totalViolation;

    private Double technicalDebt;

    private Double compliance;

}
