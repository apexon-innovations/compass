package com.apexon.compass.dashboard.collector;

import com.apexon.compass.dashboard.model.Collector;
import com.apexon.compass.dashboard.model.GenericCollectorItem;
import com.apexon.compass.dashboard.model.relation.RelatedCollectorItem;
import com.apexon.compass.dashboard.repository.CollectorItemRepository;
import com.apexon.compass.dashboard.repository.GenericCollectorItemRepository;
import com.apexon.compass.dashboard.repository.RelatedCollectorItemRepository;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public abstract class CollectorTaskWithGenericItem<T extends Collector> extends CollectorTask<T> {

	private final CollectorItemRepository collectorItemRepository;

	private final GenericCollectorItemRepository genericCollectorItemRepository;

	private final RelatedCollectorItemRepository relatedCollectorItemRepository;

	@Autowired
	public CollectorTaskWithGenericItem(TaskScheduler taskScheduler, String collectorName,
			CollectorItemRepository collectorItemRepository,
			GenericCollectorItemRepository genericCollectorItemRepository,
			RelatedCollectorItemRepository relatedCollectorItemRepository) {
		super(taskScheduler, collectorName);
		this.collectorItemRepository = collectorItemRepository;
		this.genericCollectorItemRepository = genericCollectorItemRepository;
		this.relatedCollectorItemRepository = relatedCollectorItemRepository;
	}

	public Map<ObjectId, Set<ObjectId>> processGenericItems(List<String> toolServers) {
		List<GenericCollectorItem> genericCollectorItems = genericCollectorItemRepository
			.findAllByToolNameAndProcessTimeEquals(getCollector().getName(), 0L);
		if (CollectionUtils.isEmpty(genericCollectorItems)) {
			return Collections.emptyMap();
		}
		Map<ObjectId, Set<ObjectId>> collectorItemBuildIds = new HashMap<>();
		genericCollectorItems.forEach(gci -> {
			toolServers.stream()
				.map(server -> Lists.newArrayList(collectorItemRepository.findAllByOptionMapAndCollectorIdsIn(
						getGenericCollectorItemOptions(server, gci), Lists.newArrayList(getCollector().getId()))))
				.forEach(collectorItems -> collectorItems.forEach(item -> {
					// Save as related item. Related Item event listener will process it.
					if (!collectorItemBuildIds.containsKey(item.getId())) {
						collectorItemBuildIds.put(item.getId(), new HashSet<>());
					}
					item.setEnabled(true);
					collectorItemRepository.save(item);
					collectorItemBuildIds.get(item.getId()).add(gci.getBuildId());
					RelatedCollectorItem relatedCollectorItem = new RelatedCollectorItem();
					relatedCollectorItem.setCreationTime(System.currentTimeMillis());
					relatedCollectorItem.setLeft(gci.getRelatedCollectorItem());
					relatedCollectorItem.setRight(item.getId());
					relatedCollectorItem.setSource(gci.getSource());
					relatedCollectorItemRepository.save(relatedCollectorItem);
				}));
			// Save generic item as processed, ie, processing time non zero.
			gci.setProcessTime(System.currentTimeMillis());
			genericCollectorItemRepository.save(gci);
		});
		return collectorItemBuildIds;
	}

	public abstract Map<String, Object> getGenericCollectorItemOptions(String serverUrl,
			GenericCollectorItem genericCollectorItem);

}
