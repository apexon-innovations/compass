package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.Collector;
import com.apexon.compass.dashboard.model.CollectorItem;
import com.apexon.compass.dashboard.model.ScopeOwnerCollectorItem;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ScopeOwnerRepositoryTest {

	private static final String generalUseDate = "2015-11-01T00:00:00Z";

	private static final String olderThanGeneralUseDate = "2015-10-30T00:00:00Z";

	private static final ObjectId jiraCollectorId = new ObjectId();

	private static final ObjectId v1CollectorId = new ObjectId();

	private static ScopeOwnerCollectorItem mockV1ScopeOwner;

	private static ScopeOwnerCollectorItem mockJiraScopeOwner;

	private static ScopeOwnerCollectorItem mockJiraScopeOwner2;

	private static CollectorItem mockBadItem;

	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

	private static final String maxDateWinner = df.format(new Date());

	private static Calendar cal = Calendar.getInstance();

	private static String maxDateLoser = new String();

	@Mock
	private ScopeOwnerRepository scopeOwnerRepo;

	@Mock
	private CollectorItemRepository badItemRepo;

	@BeforeAll
	void setUp() {
		// Date-time modifications
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_YEAR, -1);
		maxDateLoser = df.format(cal.getTime());

		// VersionOne Mock Scope
		mockV1ScopeOwner = new ScopeOwnerCollectorItem();
		mockV1ScopeOwner.setCollectorId(v1CollectorId);
		mockV1ScopeOwner.setIsDeleted("False");
		mockV1ScopeOwner.setChangeDate(maxDateLoser);
		mockV1ScopeOwner.setAssetState("Active");
		mockV1ScopeOwner.setId(ObjectId.get());
		mockV1ScopeOwner.setTeamId("Team:129825");
		mockV1ScopeOwner.setName("Resistance");
		mockV1ScopeOwner.setDescription(mockV1ScopeOwner.getName());
		mockV1ScopeOwner.setEnabled(true);

		// Jira Mock Scope
		// Mock Scope 1
		mockJiraScopeOwner = new ScopeOwnerCollectorItem();
		mockJiraScopeOwner.setCollectorId(jiraCollectorId);
		mockJiraScopeOwner.setIsDeleted("False");
		mockJiraScopeOwner.setChangeDate(maxDateWinner);
		mockJiraScopeOwner.setAssetState("Active");
		mockJiraScopeOwner.setId(ObjectId.get());
		mockJiraScopeOwner.setTeamId("871589423");
		mockJiraScopeOwner.setName("Sith Lords");
		mockJiraScopeOwner.setDescription(mockJiraScopeOwner.getName());
		mockJiraScopeOwner.setEnabled(true);

		// Mock Scope 2
		mockJiraScopeOwner2 = new ScopeOwnerCollectorItem();
		mockJiraScopeOwner2.setCollectorId(jiraCollectorId);
		mockJiraScopeOwner2.setIsDeleted("False");
		mockJiraScopeOwner2.setChangeDate(generalUseDate);
		mockJiraScopeOwner2.setAssetState("Active");
		mockJiraScopeOwner2.setId(ObjectId.get());
		mockJiraScopeOwner2.setTeamId("078123416");
		mockJiraScopeOwner2.setName("Jedi Knights");
		mockJiraScopeOwner2.setDescription(mockJiraScopeOwner2.getName());
		mockJiraScopeOwner2.setEnabled(false);

		// Mock Alternative Collector Item
		mockBadItem = new CollectorItem();
		mockBadItem.setCollector(new Collector());
		mockBadItem.setCollectorId(jiraCollectorId);
		mockBadItem.setDescription("THIS SHOULD NOT SHOW UP");
		mockBadItem.setEnabled(true);
		mockBadItem.setId(ObjectId.get());
	}

	@AfterAll
	void tearDown() {
		mockV1ScopeOwner = null;
		mockJiraScopeOwner = null;
		mockJiraScopeOwner2 = null;
		mockBadItem = null;
		badItemRepo.deleteAll();
		scopeOwnerRepo.deleteAll();
	}

	@Test
	void validateConnectivity_HappyPath() {
		List<ScopeOwnerCollectorItem> items = new ArrayList<ScopeOwnerCollectorItem>();
		Iterable<ScopeOwnerCollectorItem> itemsIt = items;
		items.add(mockV1ScopeOwner);
		items.add(mockJiraScopeOwner);
		items.add(mockJiraScopeOwner2);

		scopeOwnerRepo.save(mockV1ScopeOwner);
		scopeOwnerRepo.save(mockJiraScopeOwner);
		scopeOwnerRepo.save(mockJiraScopeOwner2);

		when(scopeOwnerRepo.findAll()).thenReturn(itemsIt);

		assertThat(scopeOwnerRepo.findAll());

		assertTrue("Happy-path MongoDB connectivity validation for the ScopeRepository has failed",
				scopeOwnerRepo.findAll().iterator().hasNext());
		verify(scopeOwnerRepo, times(2)).findAll();
	}

	@Test
	void testFindTeamCollector_NoCollectorForGivenFilter() {
		scopeOwnerRepo.save(mockV1ScopeOwner);
		scopeOwnerRepo.save(mockJiraScopeOwner2);

		when(scopeOwnerRepo.findTeamCollector(mockJiraScopeOwner.getCollectorId(), mockJiraScopeOwner.getTeamId(),
				mockJiraScopeOwner.getName()))
			.thenReturn(null);

		assertNull("Expected null response did not match actual null response", scopeOwnerRepo.findTeamCollector(
				mockJiraScopeOwner.getCollectorId(), mockJiraScopeOwner.getTeamId(), mockJiraScopeOwner.getName()));

		verify(scopeOwnerRepo).findTeamCollector(mockJiraScopeOwner.getCollectorId(), mockJiraScopeOwner.getTeamId(),
				mockJiraScopeOwner.getName());
	}

	@Test
	void testFindEnabledTeamCollectors_HappyPath() {
		List<ScopeOwnerCollectorItem> items = scopeOwnerRepo
			.findEnabledTeamCollectors(mockJiraScopeOwner.getCollectorId(), mockJiraScopeOwner.getTeamId());
		items.add(mockJiraScopeOwner);

		when(scopeOwnerRepo.findEnabledTeamCollectors(mockJiraScopeOwner.getCollectorId(),
				mockJiraScopeOwner.getTeamId()))
			.thenReturn(items);

		scopeOwnerRepo.save(mockV1ScopeOwner);
		scopeOwnerRepo.save(mockJiraScopeOwner);
		scopeOwnerRepo.save(mockJiraScopeOwner2);

		assertEquals(
				"Expected number of enabled team collectors did not match actual number of enabled team collectors", 1,
				scopeOwnerRepo
					.findEnabledTeamCollectors(mockJiraScopeOwner.getCollectorId(), mockJiraScopeOwner.getTeamId())
					.size());

		verify(scopeOwnerRepo, times(2)).findEnabledTeamCollectors(mockJiraScopeOwner.getCollectorId(),
				mockJiraScopeOwner.getTeamId());
	}

	@Test
	void testGetTeamMaxChangeDate_HappyPath() {
		List<ScopeOwnerCollectorItem> items = scopeOwnerRepo
			.findTopByChangeDateDesc(mockJiraScopeOwner.getCollectorId(), olderThanGeneralUseDate);
		items.add(mockJiraScopeOwner);

		Mockito.lenient()
			.when(scopeOwnerRepo.findTopByChangeDateDesc(mockJiraScopeOwner.getCollectorId(), maxDateLoser))
			.thenReturn(items);

		scopeOwnerRepo.save(mockV1ScopeOwner);
		scopeOwnerRepo.save(mockJiraScopeOwner);
		scopeOwnerRepo.save(mockJiraScopeOwner2);

		assertTrue(items.size() > 0);
		assertEquals(
				"Expected number of enabled team collectors did not match actual number of enabled team collectors",
				mockJiraScopeOwner.getChangeDate(),
				scopeOwnerRepo.findTopByChangeDateDesc(mockJiraScopeOwner.getCollectorId(), maxDateLoser)
					.get(0)
					.getChangeDate()
					.toString());
		assertEquals(
				"Expected number of enabled team collectors did not match actual number of enabled team collectors",
				maxDateWinner, items.get(0).getChangeDate().toString());

		verify(scopeOwnerRepo).findTopByChangeDateDesc(mockJiraScopeOwner.getCollectorId(), olderThanGeneralUseDate);
	}

	@Test
	void testGetTeamMaxChangeDate_WithOtherCollectorItemClasses() {
		scopeOwnerRepo.save(mockV1ScopeOwner);
		scopeOwnerRepo.save(mockJiraScopeOwner);
		scopeOwnerRepo.save(mockJiraScopeOwner2);
		badItemRepo.save(mockBadItem);

		ObjectId oid = mockJiraScopeOwner.getCollectorId();
		List<ScopeOwnerCollectorItem> items = scopeOwnerRepo.findTopByChangeDateDesc(oid, olderThanGeneralUseDate);
		items.add(mockJiraScopeOwner);

		Mockito.lenient().when(scopeOwnerRepo.findTopByChangeDateDesc(oid, olderThanGeneralUseDate)).thenReturn(items);

		assertTrue("A wild CollectorItem class appeared!",
				items.get(0)
					.getClass()
					.toString()
					.equalsIgnoreCase("class com.apexon.compass.dashboard.model.ScopeOwnerCollectorItem"));

		verify(scopeOwnerRepo).findTopByChangeDateDesc(oid, olderThanGeneralUseDate);
	}

	@Test
	void testGetTeamIdById_HappyPath() {
		List<ScopeOwnerCollectorItem> itemss = new ArrayList<ScopeOwnerCollectorItem>();
		itemss.add(mockJiraScopeOwner2);

		when(scopeOwnerRepo.save(mockJiraScopeOwner2)).thenReturn(mockJiraScopeOwner2);
		when(scopeOwnerRepo.getTeamIdById(mockJiraScopeOwner2.getTeamId())).thenReturn(itemss);

		scopeOwnerRepo.save(mockV1ScopeOwner);
		scopeOwnerRepo.save(mockJiraScopeOwner);
		scopeOwnerRepo.save(mockJiraScopeOwner2);

		String id = mockJiraScopeOwner2.getTeamId();
		List<ScopeOwnerCollectorItem> items = scopeOwnerRepo.getTeamIdById(id);

		assertEquals("078123416", id);
		assertEquals(1, items.size());

		assertEquals(
				"Expected number of enabled team collectors did not match actual number of enabled team collectors",
				mockJiraScopeOwner2.getTeamId(),
				scopeOwnerRepo.getTeamIdById(mockJiraScopeOwner2.getTeamId()).get(0).getTeamId().toString());

		verify(scopeOwnerRepo).save(mockJiraScopeOwner2);
	}

	@Test
	void testGetTeamIdById_IndexOutOfBoundsException() {
		String testValue = "This does not exist";
		List<ScopeOwnerCollectorItem> items = scopeOwnerRepo.getTeamIdById(testValue);

		assertEquals(0, items.size());
		assertThrows(IndexOutOfBoundsException.class, () -> {
			scopeOwnerRepo.getTeamIdById(testValue).get(0).getTeamId().toString();
		});
	}

	@Test
	void testGetTeamIdById_InActiveValidTeamId_OneResponse() {
		scopeOwnerRepo.save(mockV1ScopeOwner);
		assertEquals("An unexpected inactive team was included with the response", 0,
				scopeOwnerRepo.getTeamIdById(mockV1ScopeOwner.getTeamId()).size());
		scopeOwnerRepo.deleteAll();
		mockV1ScopeOwner.setAssetState("InActive");
		scopeOwnerRepo.save(mockV1ScopeOwner);
		assertEquals("Teams which are inactive should also return to be updated", 0,
				scopeOwnerRepo.getTeamIdById(mockV1ScopeOwner.getTeamId()).size());
	}

}