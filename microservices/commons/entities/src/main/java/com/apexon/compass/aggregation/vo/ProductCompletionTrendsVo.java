package com.apexon.compass.aggregation.vo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCompletionTrendsVo {

    private IdVo _id;

    private List<ProductCompletionSprintDataVo> sprintData;

}
