package com.apexon.compass.aggregation.vo;

import com.apexon.compass.entities.Approvers;
import com.apexon.compass.entities.AuthorIdAndName;
import com.apexon.compass.entities.Comments;
import com.apexon.compass.entities.PullRequestsCommits;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PullRequestsVo {

    private ObjectId _id;

    private ObjectId scmConfigurationId;

    private ObjectId projectId;

    private String scmUrl;

    private String prNumber;

    private String sourceRepoId;

    private String sourceBranch;

    private String targetRepoId;

    private String targetBranch;

    private String revisionNumber;

    private String commitLog;

    private Long prLastUpdatedTime;

    private Long prCreatedTime;

    private Long prDeclinedTime;

    private String prDeclinedBy;

    private Long prMergeTime;

    private String prMergedBy;

    private String state;

    private AuthorIdAndName scmAuthor;

    private List<AuthorIdAndName> reviewers;

    private List<Comments> comments;

    private List<Approvers> approvers;

    private List<PullRequestsCommits> commits;

}
