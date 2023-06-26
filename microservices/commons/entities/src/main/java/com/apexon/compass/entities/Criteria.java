package com.apexon.compass.entities;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Criteria {

    private String parameter;

    private String value;

    private List<Parameter> subparameters;

    private List<CommentModel> commentModels;

}
