package com.apexon.compass.entities;

import static com.apexon.compass.constants.EntitiesConstants.CUSTOMER;
import static com.apexon.compass.constants.EntitiesConstants.ISC_PROJECTS_NPS;
import static com.apexon.compass.constants.EntitiesConstants.OVERALLSATISFACTION;
import static com.apexon.compass.constants.EntitiesConstants.SURVEY;

import java.time.LocalDate;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = ISC_PROJECTS_NPS)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IscProjectsNps {

    @Id
    private String id;

    @NotBlank
    private ObjectId iscProjectId;

    @NotBlank
    private String fileName;

    @NotBlank
    private Long submissionDate;

    @NotBlank
    private LocalDate submissionPeriod;

    @NotBlank
    @CreatedBy
    private String createdBy;

    @NotBlank
    @CreatedDate
    private Long createdDate;

    @LastModifiedBy
    private String updatedBy;

    @LastModifiedDate
    private Long updatedDate;

    @NotNull
    private String teamSize;

    @NotBlank
    private String duration;

    @NotBlank
    private String conclusionRemarks;

    @Field(name = CUSTOMER)
    private Customer customer;

    @Field(name = SURVEY)
    private List<Survey> survey;

    @Field(name = OVERALLSATISFACTION)
    private List<OverallSatisfaction> overallSatisfaction;

}
