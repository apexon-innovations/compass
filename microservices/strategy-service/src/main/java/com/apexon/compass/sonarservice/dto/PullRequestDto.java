package com.apexon.compass.sonarservice.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PullRequestDto {

    private String id;

    private String number;

    private String commitLog;

    private Long createdDateTime;

    private String journeyDuration;

    private Long mergedDateTime;

    private String mergedBy;

    private Long declinedDateTime;

    private String declinedBy;

    private String state;

    private String sourceBranch;

    private String destinationBranch;

    private AuthorReviewerDto author;

    private List<ApproversDto> approvers;

    private List<AuthorReviewerDto> reviewers;

    private List<CommentsDto> comments;

    private List<CommitsDto> commits;

}
