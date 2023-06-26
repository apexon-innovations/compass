package com.apexon.compass.entities;

import static com.apexon.compass.constants.EntitiesConstants.DEFECT_AGEING_TRENDS;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = DEFECT_AGEING_TRENDS)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DefectAgeingTrends {

    @Id
    private String id;

    private String projectName;

    private ObjectId projectId;

    private Integer jiraProjectId;

    private List<DefectAgeingTrendsDataList> data;

}
