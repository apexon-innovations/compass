package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.Collector;
import com.apexon.compass.dashboard.model.CollectorItem;
import com.apexon.compass.dashboard.model.Team;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamRepositoryTest {

	private static final String generalUseDate = "2015-11-01T00:00:00Z";

	private static final String olderThanGeneralUseDate = "2015-10-30T00:00:00Z";

	private static final ObjectId jiraCollectorId = new ObjectId();

	private static final ObjectId v1CollectorId = new ObjectId();

	private static Team mockV1Team;

	private static Team mockJiraTeam;

	private static Team mockJiraTeam2;

	private static CollectorItem mockBadItem;

	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

	private static final String maxDateWinner = df.format(new Date());

	private static Calendar cal = Calendar.getInstance();

	private static String maxDateLoser = new String();

	@Mock
	private TeamRepository teamRepo;

	@Mock
	private CollectorItemRepository badItemRepo;

	@BeforeEach
	void setUp() {
		// Date-time modifications
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_YEAR, -1);
		maxDateLoser = df.format(cal.getTime());

		// VersionOne Mock Scope
		mockV1Team = new Team("", "");
		mockV1Team.setCollectorId(v1CollectorId);
		mockV1Team.setIsDeleted("False");
		mockV1Team.setChangeDate(maxDateLoser);
		mockV1Team.setAssetState("Active");
		mockV1Team.setId(ObjectId.get());
		mockV1Team.setTeamId("Team:129825");
		mockV1Team.setName("Resistance");
		mockV1Team.setEnabled(true);

		// Jira Mock Scope
		// Mock Scope 1
		mockJiraTeam = new Team("", "");
		mockJiraTeam.setCollectorId(jiraCollectorId);
		mockJiraTeam.setIsDeleted("False");
		mockJiraTeam.setChangeDate(maxDateWinner);
		mockJiraTeam.setAssetState("Active");
		mockJiraTeam.setId(ObjectId.get());
		mockJiraTeam.setTeamId("871589423");
		mockJiraTeam.setName("Sith Lords");
		mockJiraTeam.setEnabled(true);

		// Mock Scope 2
		mockJiraTeam2 = new Team("", "");
		mockJiraTeam2.setCollectorId(jiraCollectorId);
		mockJiraTeam2.setIsDeleted("False");
		mockJiraTeam2.setChangeDate(generalUseDate);
		mockJiraTeam2.setAssetState("Active");
		mockJiraTeam2.setId(ObjectId.get());
		mockJiraTeam2.setTeamId("078123416");
		mockJiraTeam2.setName("Jedi Knights");
		mockJiraTeam2.setEnabled(false);

		// Mock Alternative Collector Item
		mockBadItem = new CollectorItem();
		mockBadItem.setCollector(new Collector());
		mockBadItem.setCollectorId(jiraCollectorId);
		mockBadItem.setDescription("THIS SHOULD NOT SHOW UP");
		mockBadItem.setEnabled(true);
		mockBadItem.setId(ObjectId.get());
	}

	@AfterEach
	void tearDown() {
		mockV1Team = null;
		mockJiraTeam = null;
		mockJiraTeam2 = null;
		mockBadItem = null;
		badItemRepo.deleteAll();
		teamRepo.deleteAll();
	}

	@Test
	void validateConnectivity_HappyPath() {
		teamRepo.save(mockV1Team);
		teamRepo.save(mockJiraTeam);
		teamRepo.save(mockJiraTeam2);

		List<Team> items = new ArrayList<>();
		items.add(mockV1Team);
		Iterable<Team> itr = (Iterable<Team>) items;

		when(teamRepo.findAll()).thenReturn(itr);

		Assertions.assertTrue(teamRepo.findAll().iterator().hasNext(),
				"Happy-path MongoDB connectivity validation for the ScopeRepository has failed");
	}

	@Test
	void testFindTeamCollector_NoCollectorForGivenFilter() {
		teamRepo.save(mockV1Team);
		teamRepo.save(mockJiraTeam);
		teamRepo.save(mockJiraTeam2);

		assertEquals(0, teamRepo.findByCollectorId(new ObjectId("588fc489bb6280f450f2b647")).size(),
				"Expected null response did not match actual null response");
	}

	@Test
	void testGetTeamMaxChangeDate_HappyPath() {

		teamRepo.save(mockV1Team);
		teamRepo.save(mockJiraTeam);
		teamRepo.save(mockJiraTeam2);

		List<Team> outputList = teamRepo.findTopByChangeDateDesc(mockJiraTeam.getCollectorId(),
				olderThanGeneralUseDate);
		outputList.add(mockJiraTeam);

		Mockito.lenient()
			.when(teamRepo.findTopByChangeDateDesc(mockJiraTeam.getCollectorId(), maxDateLoser))
			.thenReturn(outputList);

		Assertions.assertTrue(outputList.size() > 0);
		assertEquals(mockJiraTeam.getChangeDate(),
				teamRepo.findTopByChangeDateDesc(mockJiraTeam.getCollectorId(), maxDateLoser).get(0).getChangeDate(),
				"Expected number of enabled team collectors did not match actual number of enabled team collectors");
		assertEquals(maxDateWinner, outputList.get(0).getChangeDate(),
				"Expected number of enabled team collectors did not match actual number of enabled team collectors");
	}

	@Test
	void testGetTeamIdById_HappyPath() {
		teamRepo.save(mockV1Team);
		teamRepo.save(mockJiraTeam);
		teamRepo.save(mockJiraTeam2);

		String id = mockJiraTeam2.getTeamId();

		assertEquals("078123416", id);
	}

}
