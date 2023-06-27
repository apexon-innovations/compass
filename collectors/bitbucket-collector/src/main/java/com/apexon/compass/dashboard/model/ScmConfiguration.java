package com.apexon.compass.dashboard.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "scm_configuration")
public class ScmConfiguration {

	public static final String REPO_URL = "url"; // http://git.company.com/jack/somejavacode

	public static final String BRANCH = "branch"; // master, development etc.

	public static final String USER_ID = "userID";

	public static final String PASSWORD = "password";

	public static final String LAST_UPDATE_TIME = "lastUpdate";

	public static final String LAST_UPDATE_COMMIT = "lastUpdateCommit"; // Bitbucket
																		// Server api uses
																		// last update
																		// commit instead
																		// of time

	@Id
	private ObjectId id;

	private ObjectId iscProjectId;

	private List<ScmRepo> scm;

	private String userName;

	private String credentials;

	private String encryptionKey;

	private String product;

	private String endPoint;

	private int codeChurnDelta;

	private int legacyRefactorDelta;

	private String source;

	private Long createdDate;

	private String createdBy;

	private Long updatedDate;

	private String updatedBy;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public ObjectId getIscProjectId() {
		return iscProjectId;
	}

	public void setIscProjectId(ObjectId iscProjectId) {
		this.iscProjectId = iscProjectId;
	}

	public List<ScmRepo> getScm() {
		return scm;
	}

	public void setScm(List<ScmRepo> scm) {
		this.scm = scm;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCredentials() {
		return credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}

	public String getEncryptionKey() {
		return encryptionKey;
	}

	public void setEncryptionKey(String encryptionKey) {
		this.encryptionKey = encryptionKey;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public int getCodeChurnDelta() {
		return codeChurnDelta;
	}

	public void setCodeChurnDelta(int codeChurnDelta) {
		this.codeChurnDelta = codeChurnDelta;
	}

	public int getLegacyRefactorDelta() {
		return legacyRefactorDelta;
	}

	public void setLegacyRefactorDelta(int legacyRefactorDelta) {
		this.legacyRefactorDelta = legacyRefactorDelta;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

}
