package com.apexon.compass.dashboard.collector;

import com.apexon.compass.dashboard.model.Sprint;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class SprintFormatter {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultJiraClient.class);

	static Sprint parseSprint(JSONArray customArray) {
		if (CollectionUtils.isEmpty(customArray)) {
			return null;
		}
		Pattern pattern = Pattern.compile("\\[(.*?)\\]");
		List<Sprint> sprints = new ArrayList<>();

		for (Object c : customArray) {
			try {
				Matcher matcher = pattern.matcher((String) c);
				while (matcher.find()) {
					String text = matcher.group(1);
					Map<String, String> sprintMap = Arrays.stream(text.split(","))
						.map(s -> s.split("="))
						.filter(s -> s.length == 2)
						.collect(toMap(s -> s[0], s -> s[1]));

					if (MapUtils.isEmpty(sprintMap)) {
						return null;
					}

					Sprint sprint = new Sprint();
					sprint.setId(MapUtils.getString(sprintMap, "id", ""));
					sprint.setName(MapUtils.getString(sprintMap, "name", ""));
					sprint.setRapidViewId(MapUtils.getString(sprintMap, "rapidView", ""));
					sprint.setStartDateStr(MapUtils.getString(sprintMap, "startDate", ""));

					boolean startDateIsEmpty = StringUtils.isEmpty(sprint.getStartDateStr());
					boolean startDateIsNullish = sprint.getStartDateStr().equalsIgnoreCase("<null>");
					if (startDateIsEmpty || startDateIsNullish) {
						LOGGER.info("INFO: Sprint start date is bad. Sprint ID={}", sprint.getId());
						continue;
					}
					sprint.setState(MapUtils.getString(sprintMap, "state", ""));
					sprint.setSequence(MapUtils.getString(sprintMap, "sequence", ""));
					sprint.setEndDateStr(MapUtils.getString(sprintMap, "endDate", ""));
					sprints.add(sprint);
				}
			}
			catch (ClassCastException e) {
				JSONObject jsonObject = (JSONObject) c;

				Sprint sprint = new Sprint();
				sprint.setId(jsonObject.get("id").toString());
				sprint.setName(jsonObject.get("name").toString());
				sprint.setRapidViewId(jsonObject.get("boardId").toString());
				sprint.setStartDateStr((String) jsonObject.get("startDate"));

				boolean startDateIsEmpty = StringUtils.isEmpty(sprint.getStartDateStr());
				if (startDateIsEmpty) {
					LOGGER.info("INFO: Sprint start date is bad. Sprint ID={}", sprint.getId());
					continue;
				}
				sprint.setState(jsonObject.get("state").toString());
				sprint.setSequence("");
				sprint.setEndDateStr((String) jsonObject.get("endDate"));
				sprints.add(sprint);
			}
			catch (Exception e) {
				throw e;
			}

		}
		if (CollectionUtils.isEmpty(sprints)) {
			return null;
		}

		// Sort by date and take the latest sprint.
		sprints = sprints.stream()
			.sorted(Comparator
				.comparing(s -> LocalDateTime.parse(s.getStartDateStr(), DateTimeFormatter.ISO_OFFSET_DATE_TIME)))
			.collect(Collectors.toList());

		return sprints.get(sprints.size() - 1);
	}

	static List<Sprint> parseSprintJourney(JSONArray customArray) {
		if (CollectionUtils.isEmpty(customArray)) {
			return null;
		}
		Pattern pattern = Pattern.compile("\\[(.*?)\\]");
		List<Sprint> sprints = new ArrayList<>();

		for (Object c : customArray) {
			try {
				Matcher matcher = pattern.matcher((String) c);
				while (matcher.find()) {
					String text = matcher.group(1);
					Map<String, String> sprintMap = Arrays.stream(text.split(","))
						.map(s -> s.split("="))
						.filter(s -> s.length == 2)
						.collect(toMap(s -> s[0], s -> s[1]));

					if (MapUtils.isEmpty(sprintMap)) {
						return null;
					}

					Sprint sprint = new Sprint();
					sprint.setId(MapUtils.getString(sprintMap, "id", ""));
					sprint.setName(MapUtils.getString(sprintMap, "name", ""));
					sprint.setRapidViewId(MapUtils.getString(sprintMap, "rapidView", ""));
					sprint.setStartDateStr(MapUtils.getString(sprintMap, "startDate", ""));

					boolean startDateIsEmpty = StringUtils.isEmpty(sprint.getStartDateStr());
					boolean startDateIsNullish = sprint.getStartDateStr().equalsIgnoreCase("<null>");
					if (startDateIsEmpty || startDateIsNullish) {
						LOGGER.info("INFO: Sprint start date is bad. Sprint ID={}", sprint.getId());
						continue;
					}
					sprint.setState(MapUtils.getString(sprintMap, "state", ""));
					sprint.setSequence(MapUtils.getString(sprintMap, "sequence", ""));
					sprint.setEndDateStr(MapUtils.getString(sprintMap, "endDate", ""));
					sprints.add(sprint);
				}
			}
			catch (ClassCastException e) {
				JSONObject jsonObject = (JSONObject) c;

				Sprint sprint = new Sprint();
				sprint.setId(jsonObject.get("id").toString());
				sprint.setName(jsonObject.get("name").toString());
				sprint.setRapidViewId(jsonObject.get("boardId").toString());
				sprint.setStartDateStr((String) jsonObject.get("startDate"));

				boolean startDateIsEmpty = StringUtils.isEmpty(sprint.getStartDateStr());
				if (startDateIsEmpty) {
					LOGGER.info("INFO: Sprint start date is bad. Sprint ID={}", sprint.getId());
					continue;
				}
				sprint.setState(jsonObject.get("state").toString());
				sprint.setSequence("");
				sprint.setEndDateStr((String) jsonObject.get("endDate"));
				sprints.add(sprint);
			}
			catch (Exception e) {
				throw e;
			}
		}
		if (CollectionUtils.isEmpty(sprints)) {
			return null;
		}

		// Sort by date and take the latest sprint.
		sprints = sprints.stream()
			.sorted(Comparator
				.comparing(s -> LocalDateTime.parse(s.getStartDateStr(), DateTimeFormatter.ISO_OFFSET_DATE_TIME)))
			.collect(Collectors.toList());

		// Remove active sprint
		sprints.remove(sprints.size() - 1);
		return sprints;
	}

}
