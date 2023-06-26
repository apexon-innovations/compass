package com.apexon.compass.aggregation.vo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientCodeMetricsVo {

    @Id
    private ObjectId _id;

    private List<CodeMetricsMonthlyDataVo> monthData;

    private String repoId;

    private String repoName;

    private Integer codeChurn;

    private Integer legacyRefactor;

}
