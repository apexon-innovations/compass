package com.apexon.compass.dashboard.model;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ComponentTest {

	@Test
	void testGetLastUpdatedCollectorItemForType() {
		Component component = new Component();
		List<CollectorItem> cItems = new ArrayList<>();
		cItems.add(makeCollectorItem(1537476665987L));
		cItems.add(makeCollectorItem(0));
		cItems.add(makeCollectorItem(1537471111111L));
		cItems.add(makeCollectorItem(1537472222222L));
		cItems.add(makeCollectorItem(1537473333333L));
		component.getCollectorItems().put(CollectorType.SCM, cItems);
		CollectorItem c = component.getLastUpdatedCollectorItemForType(CollectorType.SCM);
		assertArrayEquals(new long[] { 1537476665987L }, new long[] { c.getLastUpdated() });

	}

	@Test
	void testGetLastUpdatedCollectorItemForTypeForOneItem() {
		Component component = new Component();
		List<CollectorItem> cItems = new ArrayList<>();
		cItems.add(makeCollectorItem(1537473333333L));
		component.getCollectorItems().put(CollectorType.SCM, cItems);
		CollectorItem c = component.getLastUpdatedCollectorItemForType(CollectorType.SCM);
		assertArrayEquals(new long[] { 1537473333333L }, new long[] { c.getLastUpdated() });

	}

	@Test
	void testGetLastUpdatedCollectorItemForTypeForZero() {
		Component component = new Component();
		List<CollectorItem> cItems = new ArrayList<>();
		cItems.add(makeCollectorItem(1537473333333L));
		cItems.add(makeCollectorItem(0));
		component.getCollectorItems().put(CollectorType.SCM, cItems);
		CollectorItem c = component.getLastUpdatedCollectorItemForType(CollectorType.SCM);
		assertArrayEquals(new long[] { 1537473333333L }, new long[] { c.getLastUpdated() });

	}

	@Test
	void testGetLastUpdatedCollectorItemForTypeForZeros() {
		Component component = new Component();
		List<CollectorItem> cItems = new ArrayList<>();
		cItems.add(makeCollectorItem(0));
		cItems.add(makeCollectorItem(0));
		component.getCollectorItems().put(CollectorType.SCM, cItems);
		CollectorItem c = component.getLastUpdatedCollectorItemForType(CollectorType.SCM);
		assertArrayEquals(new long[] { 0 }, new long[] { c.getLastUpdated() });

	}

	@Test
	void testGetLastUpdatedCollectorItemForTypeForOneItemZero() {
		Component component = new Component();
		List<CollectorItem> cItems = new ArrayList<>();
		cItems.add(makeCollectorItem(0));
		component.getCollectorItems().put(CollectorType.SCM, cItems);
		CollectorItem c = component.getLastUpdatedCollectorItemForType(CollectorType.SCM);
		assertArrayEquals(new long[] { 0 }, new long[] { c.getLastUpdated() });

	}

	@Test
	void testUpdateCollectorItem() {
		Component component = createComponent(1537473333333L);
		CollectorItem c = makeCollectorItem(1537476665987L);
		c.setId(component.getCollectorItems(CollectorType.SCM).get(0).getId());
		component.updateCollectorItem(CollectorType.SCM, c);
		assertEquals(1537476665987L, component.getCollectorItems(CollectorType.SCM).get(0).getLastUpdated());
	}

	@Test
	void testUpdateCollectorItem_NoMatch() {
		Component component = createComponent(1537473333333L);
		CollectorItem c = makeCollectorItem(1537476665987L);
		component.updateCollectorItem(CollectorType.SCM, c);
		assertEquals(1537473333333L, component.getCollectorItems(CollectorType.SCM).get(0).getLastUpdated());
	}

	private CollectorItem makeCollectorItem(long lastUpdated) {
		CollectorItem c = new CollectorItem();
		c.setId(ObjectId.get());
		c.setLastUpdated(lastUpdated);
		return c;
	}

	private Component createComponent(long lastUpdated) {
		Component component = new Component();
		component.getCollectorItems()
			.put(CollectorType.SCM,
					new ArrayList<CollectorItem>(Collections.singleton(makeCollectorItem(lastUpdated))));
		return component;
	}

}
