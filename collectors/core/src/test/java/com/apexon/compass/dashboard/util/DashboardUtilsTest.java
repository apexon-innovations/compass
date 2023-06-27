package com.apexon.compass.dashboard.util;

import com.apexon.compass.dashboard.model.Collector;
import com.apexon.compass.dashboard.model.CollectorItem;
import com.apexon.compass.dashboard.model.CollectorType;
import com.apexon.compass.dashboard.model.Component;
import com.apexon.compass.dashboard.repository.CollectorItemRepository;
import com.apexon.compass.dashboard.repository.CollectorRepository;
import com.apexon.compass.dashboard.repository.ComponentRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardUtilsTest {

	private ComponentRepository componentRepository = Mockito.mock(ComponentRepository.class);

	private CollectorRepository collectorRepository = Mockito.mock(CollectorRepository.class);

	private CollectorItemRepository collectorItemRepository = Mockito.mock(CollectorItemRepository.class);

	@Autowired
	private static DashboardUtils dashboardUtils;

	@SuppressWarnings("static-access")
	@Test
	void getUniqueCollectorItemIDsFromAllComponentsTest() throws Exception {

		Set<ObjectId> uniqueIds = new HashSet<>();
		List<Component> components = new ArrayList<Component>();
		Component component1 = getComponent("TestComponent1");
		components.add(component1);
		when(componentRepository.findAll()).thenReturn(components);

		Collector collectorResult = getCollector("TestCollector1", CollectorType.Build);
		collectorResult.setId(new ObjectId("606600e707c5b9852861a4e2"));
		Collector collector = getCollector("TestCollector1", CollectorType.Build);
		collector.setId(new ObjectId("606600e707c5b9852861a4e2"));

		when(collectorRepository.save(collector)).thenReturn(collectorResult);

		CollectorItem collectorItem = getCollectorItem("TestCollectorItem1", collectorResult);
		CollectorItem collectorItem1 = getCollectorItem("TestCollectorItem1", collectorResult);
		when(collectorItemRepository.save(collectorItem)).thenReturn(collectorItem1);

		component1.addCollectorItem(collectorResult.getCollectorType(), collectorItem1);
		Collector collector11 = getCollector("TestCollector1", CollectorType.SCM);
		collector11.setId(new ObjectId("606601ec07c5b985821e571a"));

		CollectorItem collectorItem11 = getCollectorItem("TestCollectorItem11", collector11);
		collectorItem11.setCollectorId(new ObjectId("606601ec07c5b985821e571a"));
		CollectorItem collectorItem12 = getCollectorItem("TestCollectorItem11", collector11);
		collectorItem11.setCollectorId(new ObjectId("606601ec07c5b985821e571a"));
		component1.addCollectorItem(collector11.getCollectorType(), collectorItem11);
		component1.addCollectorItem(collector11.getCollectorType(), collectorItem12);

		Component component2 = getComponent("TestComponent2");
		CollectorItem collectorItem2 = getCollectorItem("TestCollectorItem2", collector11);
		component2.addCollectorItem(collector11.getCollectorType(), collectorItem2);

		Component component3 = getComponent("TestComponent2");
		component3.addCollectorItem(collector11.getCollectorType(), collectorItem2);

		uniqueIds = DashboardUtils.getUniqueCollectorItemIDsFromAllComponents(componentRepository, collectorResult);
		assertEquals(1, uniqueIds.size());
		assertTrue(uniqueIds.contains(collectorItem1.getId()));

	}

	private Component getComponent(String name) {
		Component component = new Component();
		component.setName(name);
		component.setOwner("Topo");
		return component;
	}

	private Collector getCollector(String name, CollectorType type) {
		Collector collector = new Collector();
		collector.setCollectorType(type);
		collector.setEnabled(true);
		collector.setName(name);

		return collector;
	}

	private CollectorItem getCollectorItem(String description, Collector collector) {
		CollectorItem collectorItem = new CollectorItem();
		collectorItem.setEnabled(true);
		collectorItem.setCollectorId(collector.getId());
		collectorItem.setCollector(collector);
		collectorItem.setDescription(description);
		collectorItem.setId(new ObjectId("5ba136290be2d32568777fa8"));
		return collectorItem;
	}

}