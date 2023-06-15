package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorsDataVo {

    private String _id;

    private Long lineOfCode;

    private Long codeChurn;

    private Long legacyRefactor;

    private Long addedLineOfCodeTillDate;

    private Long removedLineOfCodeTillDate;

    private Long addedLineOfCodeByAuthorInLast7Days;

    private Long removedLineOfCodeByAuthorInLast7Days;

    private Long addedLineOfCodeByAuthorInLast15Days;

    private Long removedLineOfCodeByAuthorInLast15Days;

    private Long addedLineOfCodeByAuthorInLast30Days;

    private Long removedLineOfCodeByAuthorInLast30Days;

    private Long addedLineOfCodeByAuthorInLast90Days;

    private Long removedLineOfCodeByAuthorInLast90Days;

}
