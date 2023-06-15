package com.apexon.compass.entities;

import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;
import static com.apexon.compass.constants.EntitiesConstants.SECURITY_STATS;
import lombok.Builder;
import lombok.Data;

@Document(collection = SECURITY_STATS)
@Data
@Builder
public class SecurityStats {

    private String id;

    private String projectId;

    private String analysisType;

    private long jenkinsUpdatedDateTime;

    private Integer totalCves;

    private ScanInformation scanInformation;

    private List<Summary> summary;

    private ZapSummary zapSummary;

    private DockerImageSummary dockerImageSummary;

}
