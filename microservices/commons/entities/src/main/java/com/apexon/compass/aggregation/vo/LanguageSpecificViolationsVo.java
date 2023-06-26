package com.apexon.compass.aggregation.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LanguageSpecificViolationsVo {

    private String language;

    private Integer violations;

}
