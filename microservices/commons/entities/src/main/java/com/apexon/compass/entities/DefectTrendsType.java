package com.apexon.compass.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.apexon.compass.constants.EntitiesConstants.DERIVED_STORIES;

@Document(collection = DERIVED_STORIES)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DefectTrendsType {

    @Id
    private String id;

    private String projectName;

    private ObjectId projectId;

    private Integer jiraProjectId;

    private Long date;

    private Integer openBug;

    private Integer openMajor;

    private Integer openMinor;

    private Integer openBlocker;

    private Integer openCritical;

    private Integer closeBug;

    private Integer closeMajor;

    private Integer closeMinor;

    private Integer closeBlocker;

    private Integer closeCritical;

    private Integer reopenBug;

    private Integer reopenMajor;

    private Integer reopenMinor;

    private Integer reopenBlocker;

    private Integer reopenCritical;

    private Integer rejectedBug;

    private Integer rejectedMajor;

    private Integer rejectedMinor;

    private Integer ejectedBlocker;

    private Integer rejectedCritical;

    private Integer attendedBug;

    private Integer attendedMajor;

    private Integer attendedMinor;

    private Integer attendedBlocker;

    private Integer attendedCritical;

    private Integer unattendedBug;

    private Integer unattendedMajor;

    private Integer unattendedMinor;

    private Integer unattendedBlocker;

    private Integer unattendedCritical;

}
