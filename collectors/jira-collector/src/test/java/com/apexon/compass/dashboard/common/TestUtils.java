package com.apexon.compass.dashboard.common;

import com.apexon.compass.dashboard.model.Feature;
import com.apexon.compass.dashboard.model.FeatureCollector;
import com.apexon.compass.dashboard.model.Scope;
import com.apexon.compass.dashboard.model.Team;
import com.apexon.compass.dashboard.repository.FeatureCollectorRepository;
import com.apexon.compass.dashboard.repository.FeatureRepository;
import com.apexon.compass.dashboard.repository.ScopeRepository;
import com.apexon.compass.dashboard.repository.TeamRepository;
import com.apexon.compass.dashboard.testutil.GsonUtil;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.List;

public class TestUtils {

	public static void loadCollectorFeature(FeatureCollectorRepository featureCollectorRepository) throws IOException {
		Gson gson = GsonUtil.getGson();
		String json = IOUtils.toString(Resources.getResource("./collectors/featureCollector.json"));
		FeatureCollector feature = gson.fromJson(json, new TypeToken<FeatureCollector>() {
		}.getType());
		featureCollectorRepository.save(feature);
	}

	public static void loadFeature(FeatureRepository featureRepository) throws IOException {
		Gson gson = GsonUtil.getGson();
		String json = IOUtils.toString(Resources.getResource("./feature/feature.json"));
		List<Feature> feature = gson.fromJson(json, new TypeToken<List<Feature>>() {
		}.getType());
		featureRepository.saveAll(feature);
	}

	public static void loadTeams(TeamRepository teamRepository) throws IOException {
		Gson gson = GsonUtil.getGson();
		String json = IOUtils.toString(Resources.getResource("./team/team.json"));
		List<Team> team = gson.fromJson(json, new TypeToken<List<Team>>() {
		}.getType());
		teamRepository.saveAll(team);
	}

	public static void loadScope(ScopeRepository projectRepository) throws IOException {
		Gson gson = GsonUtil.getGson();
		String json = IOUtils.toString(Resources.getResource("./scope/scope.json"));
		List<Scope> team = gson.fromJson(json, new TypeToken<List<Scope>>() {
		}.getType());
		projectRepository.saveAll(team);
	}

}
