package com.apexon.compass.dashboard.collector;

import com.apexon.compass.dashboard.model.CodeQuality;
import com.apexon.compass.dashboard.model.SonarProject;

import java.util.List;

import com.apexon.compass.dashboard.model.DesCodeQuality;
import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

public interface SonarClient {

	/**
	 * register server credentials before calling getProjects username and password
	 * override token when all arguments are not blank
	 * @param username for subsequent requests to sonarqube
	 * @param password for subsequent requests to sonarqube
	 * @param token for subsequent requests to sonarqube
	 */
	void setServerCredentials(String username, String password, String token);

	List<SonarProject> getProjects(String instanceUrl);

	CodeQuality currentCodeQuality(SonarProject project, String metrics);

	DesCodeQuality currentDesCodeQuality(SonarProject project, ObjectId sonarConfigurationId, ObjectId iscProjectId,
			String metrics);

	JSONArray getQualityProfiles(String instanceUrl) throws ParseException;

	List<String> retrieveProfileAndProjectAssociation(String instanceUrl, String qualityProfile) throws ParseException;

	JSONArray getQualityProfileConfigurationChanges(String instanceUrl, String qualityProfile) throws ParseException;

}
