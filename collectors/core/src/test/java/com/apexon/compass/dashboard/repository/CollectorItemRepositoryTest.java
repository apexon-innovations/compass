package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.CollectorItem;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CollectorItemRepositoryTest {

	private CollectorItemRepository collectorItemRepository = Mockito.mock(CollectorItemRepository.class);

	@Test
	void findAllByOptionNameValueAndCollectorIdsInReturns0() throws IOException {
		doNothing().when(collectorItemRepository).deleteAll();

		List<CollectorItem> itemList = new ArrayList<CollectorItem>();

		when(collectorItemRepository.findAllByOptionNameValueAndCollectorIdsIn("jobName", "job/",
				Collections.singletonList(new ObjectId("5ba136220be2d32568777fa4"))))
			.thenReturn(itemList);
		assertEquals(0, itemList.size());
	}

	@Test
	void findAllByOptionNameValueAndCollectorIdsInReturns1() throws IOException {
		doNothing().when(collectorItemRepository).deleteAll();

		List<CollectorItem> itemList = new ArrayList<CollectorItem>();
		CollectorItem item = new CollectorItem();
		item.setId(new ObjectId("5ba136220be2d32568777fa4"));
		itemList.add(item);

		when(collectorItemRepository.findAllByOptionNameValueAndCollectorIdsIn("jobName", "job/c1usercheck/",
				Collections.singletonList(new ObjectId("5ba136220be2d32568777fa4"))))
			.thenReturn(itemList);
		assertEquals(1, itemList.size());
		assertEquals("5ba136220be2d32568777fa4", itemList.get(0).getId().toHexString());
	}

	@Test
	void findAllByOptionNameValueAndCollectorIdsInReturns2() throws IOException {
		doNothing().when(collectorItemRepository).deleteAll();

		List<CollectorItem> itemList = new ArrayList<CollectorItem>();
		CollectorItem item = new CollectorItem();
		item.setId(new ObjectId("5ba136220be2d32568777fa5"));

		CollectorItem item2 = new CollectorItem();
		item.setId(new ObjectId("5ba136220be2d32568777fa6"));

		itemList.add(item);
		itemList.add(item2);

		when(collectorItemRepository.findAllByOptionNameValueAndCollectorIdsIn("instanceUrl", "http://localhost:8082/",
				Collections.singletonList(new ObjectId("5ba136220be2d32568777fa4"))))
			.thenReturn(itemList);

		assertEquals(2, itemList.size());

	}

	@Test
	void findAllByOptionMapAndCollectorIdsIn2MapEntry() throws IOException {
		doNothing().when(collectorItemRepository).deleteAll();

		Map<String, Object> inMap = new HashMap<>();
		inMap.put("jobName", "job/c1usercheck/");
		inMap.put("instanceUrl", "http://localhost:8082/");

		List<CollectorItem> itemList = new ArrayList<CollectorItem>();
		CollectorItem item = new CollectorItem();
		item.setId(new ObjectId("5ba136220be2d32568777fa5"));

		itemList.add(item);

		when(collectorItemRepository.findAllByOptionMapAndCollectorIdsIn(inMap,
				Collections.singletonList(new ObjectId("5ba136220be2d32568777fa4"))))
			.thenReturn(itemList);
		assertEquals(1, itemList.size());
		assertEquals("5ba136220be2d32568777fa5", itemList.get(0).getId().toHexString());
	}

	@Test
	void findAllByOptionMapAndCollectorIdsInStringAndNumber() throws IOException {
		doNothing().when(collectorItemRepository).deleteAll();
		Map<String, Object> inMap = new HashMap<>();
		inMap.put("jobName", "jobname");
		inMap.put("jobNumber", 123456789);

		List<CollectorItem> itemList = new ArrayList<CollectorItem>();
		CollectorItem item = new CollectorItem();
		item.setId(new ObjectId("5ba136220be2d32568777fa7"));
		itemList.add(item);

		when(collectorItemRepository.findAllByOptionMapAndCollectorIdsIn(inMap,
				Collections.singletonList(new ObjectId("5ba136220be2d32568777fff"))))
			.thenReturn(itemList);
		assertEquals(1, itemList.size());
		assertEquals("5ba136220be2d32568777fa7", itemList.get(0).getId().toHexString());
	}

}