package com.apexon.compass.dashboard.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

/**
 * Binary artifacts produced by build jobs and stored in an artifact repository.
 *
 * Possible collectors: Nexus (in scope) Artifactory npm nuget rubygems
 *
 */
@Document(collection = "artifacts")
public class BinaryArtifact extends BaseModel {

	// list of known metadata properties
	// Note: these may be hard coded in other modules and external tools
	private static final String METADATA_BUILD_URL = "buildUrl";

	private static final String METADATA_BUILD_NUMBER = "buildNumber";

	private static final String METADATA_JOB_URL = "jobUrl";

	private static final String METADATA_JOB_NAME = "jobName";

	private static final String METADATA_INSTANCE_URL = "instanceUrl";

	private static final String METADATA_SCM_URL = "scmUrl";

	private static final String METADATA_SCM_BRANCH = "scmBranch";

	private static final String METADATA_SCM_REVISION_NUMBER = "scmRevisionNumber";

	/**
	 * CollectorItemId for the {@link Build} that produced the artifact
	 */
	private ObjectId collectorItemId;

	private long timestamp;

	private String canonicalName;

	private String artifactGroupId;

	private String artifactModule;

	private String artifactVersion;

	private String artifactName;

	private String artifactClassifier;

	private String artifactExtension;

	private String type;

	private String repo;

	private String path;

	private long createdTimeStamp;

	private String createdBy;

	private long modifiedTimeStamp;

	private String modifiedBy;

	private String actual_sha1;

	private String actual_md5;

	private String authorLDAPDN;

	private List<Build> buildInfos = new ArrayList<>();

	private List<String> virtualRepos;

	private Map<String, String> metadata = new HashMap<>();

	public ObjectId getCollectorItemId() {
		return collectorItemId;
	}

	public void setCollectorItemId(ObjectId collectorItemId) {
		this.collectorItemId = collectorItemId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getArtifactName() {
		return artifactName;
	}

	public void setArtifactName(String artifactName) {
		this.artifactName = artifactName;
	}

	public String getArtifactGroupId() {
		return artifactGroupId;
	}

	public void setArtifactGroupId(String artifactGroupId) {
		this.artifactGroupId = artifactGroupId;
	}

	public String getArtifactVersion() {
		return artifactVersion;
	}

	public void setArtifactVersion(String artifactVersion) {
		this.artifactVersion = artifactVersion;
	}

	public String getCanonicalName() {
		return canonicalName;
	}

	public void setCanonicalName(String canonicalName) {
		this.canonicalName = canonicalName;
	}

	/**
	 * @return the artifactModule
	 */
	public String getArtifactModule() {
		return artifactModule;
	}

	/**
	 * @param artifactModule the artifactModule to set
	 */
	public void setArtifactModule(String artifactModule) {
		this.artifactModule = artifactModule;
	}

	/**
	 * @return the artifactClassifier
	 */
	public String getArtifactClassifier() {
		return artifactClassifier;
	}

	/**
	 * @param artifactClassifier the artifactClassifier to set
	 */
	public void setArtifactClassifier(String artifactClassifier) {
		this.artifactClassifier = artifactClassifier;
	}

	/**
	 * @return the artifactExtension
	 */
	public String getArtifactExtension() {
		return artifactExtension;
	}

	/**
	 * @param artifactExtension the artifactExtension to set
	 */
	public void setArtifactExtension(String artifactExtension) {
		this.artifactExtension = artifactExtension;
	}

	public String getBuildUrl() {
		return getMetadata().get(METADATA_BUILD_URL);
	}

	public void setBuildUrl(String buildUrl) {
		getMetadata().put(METADATA_BUILD_URL, buildUrl);
	}

	public String getBuildNumber() {
		return getMetadata().get(METADATA_BUILD_NUMBER);
	}

	public void setBuildNumber(String buildNumber) {
		getMetadata().put(METADATA_BUILD_NUMBER, buildNumber);
	}

	public String getJobUrl() {
		return getMetadata().get(METADATA_JOB_URL);
	}

	public void setJobUrl(String jobUrl) {
		getMetadata().put(METADATA_JOB_URL, jobUrl);
	}

	public String getJobName() {
		return getMetadata().get(METADATA_JOB_NAME);
	}

	public void setJobName(String jobName) {
		getMetadata().put(METADATA_JOB_NAME, jobName);
	}

	public String getInstanceUrl() {
		return getMetadata().get(METADATA_INSTANCE_URL);
	}

	public void setInstanceUrl(String instanceUrl) {
		getMetadata().put(METADATA_INSTANCE_URL, instanceUrl);
	}

	public String getScmUrl() {
		return getMetadata().get(METADATA_SCM_URL);
	}

	public void setScmUrl(String scmUrl) {
		getMetadata().put(METADATA_SCM_URL, scmUrl);
	}

	public String getScmBranch() {
		return getMetadata().get(METADATA_SCM_BRANCH);
	}

	public void setScmBranch(String scmBranch) {
		getMetadata().put(METADATA_SCM_BRANCH, scmBranch);
	}

	public String getScmRevisionNumber() {
		return getMetadata().get(METADATA_SCM_REVISION_NUMBER);
	}

	public void setScmRevisionNumber(String scmRevisionNumber) {
		getMetadata().put(METADATA_SCM_REVISION_NUMBER, scmRevisionNumber);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRepo() {
		return repo;
	}

	public void setRepo(String repo) {
		this.repo = repo;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getCreatedTimeStamp() {
		return createdTimeStamp;
	}

	public void setCreatedTimeStamp(long createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public long getModifiedTimeStamp() {
		return modifiedTimeStamp;
	}

	public void setModifiedTimeStamp(long modifiedTimeStamp) {
		this.modifiedTimeStamp = modifiedTimeStamp;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getActual_sha1() {
		return actual_sha1;
	}

	public void setActual_sha1(String actual_sha1) {
		this.actual_sha1 = actual_sha1;
	}

	public String getActual_md5() {
		return actual_md5;
	}

	public void setActual_md5(String actual_md5) {
		this.actual_md5 = actual_md5;
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}

	public String getAuthorLDAPDN() {
		return authorLDAPDN;
	}

	public void setAuthorLDAPDN(String authorLDAPDN) {
		this.authorLDAPDN = authorLDAPDN;
	}

	public void setVirtualRepos(List<String> virtualRepos) {
		this.virtualRepos = virtualRepos;
	}

	public List<String> getVirtualRepos() {
		return virtualRepos;
	}

	public List<Build> getBuildInfos() {
		return buildInfos;
	}

	public void addBuild(Build build) {
		getBuildInfos().add(build);
	}

	public void setBuildInfos(List<Build> buildInfos) {
		this.buildInfos = buildInfos;
	}

	public static final Comparator<BinaryArtifact> TIMESTAMP_COMPARATOR = new Comparator<BinaryArtifact>() {
		@Override
		public int compare(BinaryArtifact o1, BinaryArtifact o2) {
			return Long.compare(o1.getTimestamp(), o2.getTimestamp());
		}
	};

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || !(obj instanceof BinaryArtifact))
			return false;

		BinaryArtifact that = (BinaryArtifact) obj;
		return Objects.equals(getArtifactName(), that.getArtifactName())
				&& Objects.equals(getArtifactVersion(), that.getArtifactVersion());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getArtifactName(), getArtifactVersion());
	}

}
