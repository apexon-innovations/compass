package com.apexon.compass.aggregation.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsolidatedDefectRatioVo {

    private IdVo _id;

    @JsonProperty
    private Integer sprintId;

    @JsonProperty
    private ArrayList<String> bugTypes;

    @JsonProperty
    private ArrayList<String> definitionOfDone;

    @JsonProperty
    private ArrayList<String> definitionOfAccepted;

    @JsonProperty
    private List<TicketStatusVo> data;

}
