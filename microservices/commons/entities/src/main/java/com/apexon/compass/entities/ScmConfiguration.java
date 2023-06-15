package com.apexon.compass.entities;

import static com.apexon.compass.constants.EntitiesConstants.SCM_CONFIGURATION;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = SCM_CONFIGURATION)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScmConfiguration {

    @Id
    private String id;

    private ObjectId projectId;

    private List<Scm> scm;

    private String userName;

    private String credentials;

    private String encryptionKey;

    private Integer codeChurnDelta;

    private Integer legacyRefactorDelta;

    private Long createdDate;

    private String createdBy;

    private Long updatedDate;

    private String updatedBy;

    private String product;

    private String source;

}
