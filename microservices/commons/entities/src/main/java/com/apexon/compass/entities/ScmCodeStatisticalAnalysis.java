package com.apexon.compass.entities;

import static com.apexon.compass.constants.EntitiesConstants.SCM_CODE_STATISTICAL_ANALYSIS;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = SCM_CODE_STATISTICAL_ANALYSIS)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScmCodeStatisticalAnalysis {

    @Id
    private String id;

    private ObjectId projectId;

    private ObjectId scmConfigurationId;

    private String repoName;

    private String repoUrl;

    private Long totalLineOfCode;

    private List<CodeState> codeChurn;

    private List<CodeState> legacyRefactor;

    private Overall overall;

    private Boolean isArchive;

    private Long createdDate;

    private String createdBy;

    private Long updatedDate;

    private String updatedBy;

}
