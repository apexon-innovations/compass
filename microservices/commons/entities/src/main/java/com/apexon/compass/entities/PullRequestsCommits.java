package com.apexon.compass.entities;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import static com.apexon.compass.constants.EntitiesConstants.PULL_REQUESTS_COMMITS;

@Document(collection = PULL_REQUESTS_COMMITS)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PullRequestsCommits {

    @Id
    String id;

    private ObjectId projectId;

    private Boolean isFirstEverCommit;

    private Long time;

    private String scmUrl;

    private String revisionNumber;

    private String message;

    private List<String> parentRevisionNumber;

    private String type;

    private String authorEmail;

    private String authorUserId;

    private ObjectId pullRequestId;

}
