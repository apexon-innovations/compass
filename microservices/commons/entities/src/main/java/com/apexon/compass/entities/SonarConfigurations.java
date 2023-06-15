package com.apexon.compass.entities;

import static com.apexon.compass.constants.EntitiesConstants.SONAR_CONFIGURATIONS;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = SONAR_CONFIGURATIONS)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SonarConfigurations {

    @Id
    private ObjectId id;

    private ObjectId projectId; // Replaced with projectId

    private List<Projects> projects;

    private String userName;

    private String credentials;

    private String encryptionKey;

    private List<String> metricsFields;

    @CreatedBy
    String createdBy;

    @CreatedDate
    Long createdDate;

    @LastModifiedBy
    String updatedBy;

    @LastModifiedDate
    Long updatedDate;

}
