package com.apexon.compass.dashboard.util;

import com.apexon.compass.dashboard.model.*;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by syq410 on 2/23/17.
 */
public class TestUtils {

	private static final ObjectId DASHBOARD_ID = new ObjectId();

	public static Commit createCommit(String revisionNumber, String scmUrl) {
		Commit commit = new Commit();
		commit.setScmRevisionNumber(revisionNumber);
		commit.setCollectorItemId(ObjectId.get());
		commit.setType(CommitType.New);
		commit.setScmUrl(scmUrl);
		return commit;
	}

	public static Pipeline getPipeline(ObjectId collectorItemId) {
		Pipeline pipeline = new Pipeline();
		pipeline.addCommit(PipelineStage.COMMIT.getName(), createPipelineCommit("scmRev3"));
		EnvironmentStage environmentStage = new EnvironmentStage();
		environmentStage.setLastArtifact(getBinaryArtifact());
		pipeline.getEnvironmentStageMap().put("DEV", environmentStage);
		Set<Build> failedBuilds = new HashSet<>();
		Build failedBuild = createBuild();
		failedBuild.setCollectorItemId(collectorItemId);
		failedBuilds.add(failedBuild);
		pipeline.setFailedBuilds(failedBuilds);
		return pipeline;
	}

	public static PipelineCommit createPipelineCommit(String revisionNumber) {
		PipelineCommit commit = new PipelineCommit();
		commit.setScmRevisionNumber(revisionNumber);
		return commit;
	}

	public static BinaryArtifact getBinaryArtifact() {
		BinaryArtifact binaryArtifact = new BinaryArtifact();
		binaryArtifact.setTimestamp(374268428);
		binaryArtifact.setBuildInfos(Arrays.asList(createBuild()));
		return binaryArtifact;
	}

	public static Build createBuild() {
		Build build = new Build();
		build.setBuildStatus(BuildStatus.Success);
		build.setNumber("1");
		build.setCollectorItemId(new ObjectId());
		List<SCM> sourceChangeSet = new ArrayList();
		sourceChangeSet.add(getScm("scmRev1"));
		sourceChangeSet.add(getScm("scmRev2"));
		build.setSourceChangeSet(sourceChangeSet);
		build.setStartTime(12286435);
		RepoBranch repoBranch = new RepoBranch();
		repoBranch.setUrl("http://github.com/scmurl");
		List<RepoBranch> repos = new ArrayList<>();
		repos.add(repoBranch);
		build.setCodeRepos(repos);
		return build;
	}

	public static SCM getScm(String scmRevNumber) {
		SCM scm = new SCM();
		scm.setScmRevisionNumber(scmRevNumber);
		return scm;
	}

}
