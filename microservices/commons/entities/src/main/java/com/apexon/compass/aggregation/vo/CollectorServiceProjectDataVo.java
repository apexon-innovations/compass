package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * @author amit.bhoraniya
 * @created 14/12/20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CollectorServiceProjectDataVo {

    @Id
    private String id;

    private String name;

    private String fileName;

    private boolean isJiraAvailable;

    private boolean isSonarAvailable;

    private boolean isSCMAvailable;

    private String scmSource;

}
