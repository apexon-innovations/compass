package com.apexon.compass.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentModel {

    private String comment;

    private String commentedBy;

    private Long commentedDate;

}
