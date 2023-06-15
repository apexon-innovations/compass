package com.apexon.compass.entities;

import static com.apexon.compass.constants.EntitiesConstants.PULL_REQUESTS;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = PULL_REQUESTS)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PullRequests {

    @Id
    private String id;

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

}
