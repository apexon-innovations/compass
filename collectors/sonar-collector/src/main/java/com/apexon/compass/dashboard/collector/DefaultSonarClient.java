package com.apexon.compass.dashboard.collector;

import com.apexon.compass.dashboard.client.RestClient;
import com.apexon.compass.dashboard.client.RestUserInfo;
import com.apexon.compass.dashboard.model.*;
import com.apexon.compass.dashboard.util.SonarDashboardUrl;
import com.apexon.compass.dashboard.model.DesCodeQuality;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component("DefaultSonarClient")
public class DefaultSonarClient implements SonarClient {

	private static final Log LOG = LogFactory.getLog(DefaultSonarClient.class);

	protected static final String URL_RESOURCES = "/api/resources?format=json";

	protected static final String URL_RESOURCE_DETAILS = "/api/resources?format=json&resource=%s&metrics=%s&includealerts=true";

	protected static final String URL_QUALITY_PROFILES = "/api/qualityprofiles/search";

	protected static final String URL_QUALITY_PROFILE_PROJECT_DETAILS = "/api/qualityprofiles/projects?key=";

	protected static final String URL_QUALITY_PROFILE_CHANGES = "/api/qualityprofiles/changelog?profileKey=";

	protected static final String DEFAULT_METRICS = "ncloc,line_coverage,violations,critical_violations,major_violations,blocker_violations,violations_density,sqale_index,test_success_density,test_failures,test_errors,tests";

	protected static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

	protected static final String ID = "id";

	protected static final String NAME = "name";

	protected static final String KEY = "key";

	protected static final String VERSION = "version";

	protected static final String MSR = "msr";

	protected static final String ALERT = "alert";

	protected static final String ALERT_TEXT = "alert_text";

	protected static final String VALUE = "val";

	protected static final String FORMATTED_VALUE = "frmt_val";

	protected static final String STATUS_WARN = "WARN";

	protected static final String STATUS_ALERT = "ALERT";

	protected static final String DATE = "date";

	protected final RestClient restClient;

	protected RestUserInfo userInfo;

	@Autowired
	public DefaultSonarClient(RestClient restClient, SonarSettings settings) {
		this.restClient = restClient;

	}

	@Override
	public void setServerCredentials(String username, String password, String token) {
		// use token when given
		if (StringUtils.isNotBlank(token)) {
			this.userInfo.setToken(token);
			this.userInfo.setUserId(null);
			this.userInfo.setPassCode(null);
		}

		// but username and password override token
		if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
			this.userInfo = new RestUserInfo(username, password);
		}

		if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
			LOG.error("Only one mode of authentication is needed. Either token or username/password. "
					+ "Both modes were detected. Using username/password");
		}
	}

	@Override
	public List<SonarProject> getProjects(String instanceUrl) {
		List<SonarProject> projects = new ArrayList<>();
		String url = instanceUrl + URL_RESOURCES;

		try {

			for (Object obj : parseAsArray(url)) {
				JSONObject prjData = (JSONObject) obj;

				SonarProject project = new SonarProject();
				project.setInstanceUrl(instanceUrl);
				project.setProjectId(str(prjData, ID));
				project.setProjectName(str(prjData, NAME));
				project.setProjectKey(str(prjData, KEY));
				projects.add(project);
			}

		}
		catch (ParseException e) {
			LOG.error("Could not parse response from: " + url, e);
		}
		catch (RestClientException rce) {
			LOG.error(rce);
		}

		return projects;
	}

	@Override
	public CodeQuality currentCodeQuality(SonarProject project, String metrics) {
		String url = String.format(project.getInstanceUrl() + URL_RESOURCE_DETAILS, project.getProjectId(), metrics);

		try {
			JSONArray jsonArray = parseAsArray(url);

			if (!jsonArray.isEmpty()) {
				JSONObject prjData = (JSONObject) jsonArray.get(0);

				CodeQuality codeQuality = new CodeQuality();
				codeQuality.setName(str(prjData, NAME));
				codeQuality.setUrl(new SonarDashboardUrl(project.getInstanceUrl(), project.getProjectId()).toString());
				codeQuality.setType(CodeQualityType.StaticAnalysis);
				codeQuality.setTimestamp(timestamp(prjData, DATE));
				codeQuality.setVersion(str(prjData, VERSION));

				for (Object metricObj : (JSONArray) prjData.get(MSR)) {
					JSONObject metricJson = (JSONObject) metricObj;

					CodeQualityMetric metric = new CodeQualityMetric(str(metricJson, KEY));
					metric.setValue(str(metricJson, VALUE));
					metric.setFormattedValue(str(metricJson, FORMATTED_VALUE));
					metric.setStatus(metricStatus(str(metricJson, ALERT)));
					metric.setStatusMessage(str(metricJson, ALERT_TEXT));
					codeQuality.getMetrics().add(metric);
				}

				return codeQuality;
			}

		}
		catch (ParseException e) {
			LOG.error("Could not parse response from: " + url, e);
		}
		catch (RestClientException rce) {
			LOG.error(rce);
		}

		return null;
	}

	@Override
	public DesCodeQuality currentDesCodeQuality(SonarProject project, ObjectId sonarConfigurationId,
			ObjectId iscProjectId, String metrics) {
		String url = String.format(project.getInstanceUrl() + URL_RESOURCE_DETAILS, project.getProjectId(), metrics);

		try {
			JSONArray jsonArray = parseAsArray(url);

			if (!jsonArray.isEmpty()) {
				JSONObject prjData = (JSONObject) jsonArray.get(0);

				DesCodeQuality desCodeQuality = new DesCodeQuality();
				desCodeQuality.setSonarProjectId(project.getProjectId());
				desCodeQuality.setSonarConfigurationId(sonarConfigurationId.toString());
				desCodeQuality.setIscProjectId(iscProjectId.toString());
				desCodeQuality.setName(str(prjData, NAME));
				desCodeQuality
					.setUrl(new SonarDashboardUrl(project.getInstanceUrl(), project.getProjectId()).toString());
				desCodeQuality.setType(CodeQualityType.StaticAnalysis);
				desCodeQuality.setTimestamp(timestamp(prjData, DATE));
				desCodeQuality.setVersion(str(prjData, VERSION));

				for (Object metricObj : (JSONArray) prjData.get(MSR)) {
					JSONObject metricJson = (JSONObject) metricObj;

					CodeQualityMetric metric = new CodeQualityMetric(str(metricJson, KEY));
					metric.setValue(str(metricJson, VALUE));
					metric.setFormattedValue(str(metricJson, FORMATTED_VALUE));
					metric.setStatus(metricStatus(str(metricJson, ALERT)));
					metric.setStatusMessage(str(metricJson, ALERT_TEXT));
					desCodeQuality.getMetrics().add(metric);
				}

				return desCodeQuality;
			}

		}
		catch (ParseException e) {
			LOG.error("Could not parse response from: " + url, e);
		}
		catch (RestClientException rce) {
			LOG.error(rce);
		}

		return null;
	}

	public JSONArray getQualityProfiles(String instanceUrl) throws ParseException {
		String url = instanceUrl + URL_QUALITY_PROFILES;
		try {
			JSONArray qualityProfileData = parseAsArray(url, "profiles");
			return qualityProfileData;
		}
		catch (ParseException e) {
			LOG.error("Could not parse response from: " + url, e);
			throw e;
		}
		catch (RestClientException rce) {
			LOG.error(rce);
			throw rce;
		}
	}

	public List<String> retrieveProfileAndProjectAssociation(String instanceUrl, String qualityProfile)
			throws ParseException {
		List<String> projects = new ArrayList<>();
		String url = instanceUrl + URL_QUALITY_PROFILE_PROJECT_DETAILS + qualityProfile;
		try {
			JSONArray associatedProjects = this.parseAsArray(url, "results");
			if (!CollectionUtils.isEmpty(associatedProjects)) {
				for (Object project : associatedProjects) {
					JSONObject projectJson = (JSONObject) project;
					String projectName = (String) projectJson.get("name");
					projects.add(projectName);
				}
				return projects;
			}
			return null;
		}
		catch (ParseException e) {
			LOG.error("Could not parse response from: " + url, e);
			throw e;
		}
		catch (RestClientException rce) {
			LOG.error(rce);
			throw rce;
		}
	}

	public JSONArray getQualityProfileConfigurationChanges(String instanceUrl, String qualityProfile)
			throws ParseException {
		String url = instanceUrl + URL_QUALITY_PROFILE_CHANGES + qualityProfile;
		try {
			JSONArray qualityProfileConfigChanges = this.parseAsArray(url, "events");
			return qualityProfileConfigChanges;
		}
		catch (ParseException e) {
			LOG.error("Could not parse response from: " + url, e);
			throw e;
		}
		catch (RestClientException rce) {
			LOG.error(rce);
			throw rce;
		}
	}

	protected JSONArray parseAsArray(String url) throws ParseException {
		ResponseEntity<String> response = restClient.makeRestCallGet(url, this.userInfo);
		return (JSONArray) new JSONParser().parse(response.getBody());
	}

	protected JSONArray parseAsArray(String url, String key) throws ParseException {
		ResponseEntity<String> response = restClient.makeRestCallGet(url, this.userInfo);
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody());
		LOG.debug(url);
		return (JSONArray) jsonObject.get(key);
	}

	protected long timestamp(JSONObject json, String key) {
		Object obj = json.get(key);
		if (obj != null) {
			try {
				return new SimpleDateFormat(DATE_FORMAT).parse(obj.toString()).getTime();
			}
			catch (java.text.ParseException e) {
				LOG.error(obj + " is not in expected format " + DATE_FORMAT, e);
			}
		}
		return 0;
	}

	protected String str(JSONObject json, String key) {
		Object obj = json.get(key);
		return obj == null ? null : obj.toString();
	}

	@SuppressWarnings("unused")
	protected Integer integer(JSONObject json, String key) {
		Object obj = json.get(key);
		return obj == null ? null : (Integer) obj;
	}

	@SuppressWarnings("unused")
	protected BigDecimal decimal(JSONObject json, String key) {
		Object obj = json.get(key);
		return obj == null ? null : new BigDecimal(obj.toString());
	}

	@SuppressWarnings("unused")
	protected Boolean bool(JSONObject json, String key) {
		Object obj = json.get(key);
		return obj == null ? null : Boolean.valueOf(obj.toString());
	}

	protected CodeQualityMetricStatus metricStatus(String status) {
		if (StringUtils.isBlank(status)) {
			return CodeQualityMetricStatus.Ok;
		}

		switch (status) {
			case STATUS_WARN:
				return CodeQualityMetricStatus.Warning;
			case STATUS_ALERT:
				return CodeQualityMetricStatus.Alert;
			default:
				return CodeQualityMetricStatus.Ok;
		}
	}

}
