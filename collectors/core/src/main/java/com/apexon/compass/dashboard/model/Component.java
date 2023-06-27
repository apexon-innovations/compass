package com.apexon.compass.dashboard.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

/**
 * A self-contained, independently deployable piece of the larger application. Each
 * component of an application has a different source repo, build job, deploy job, etc.
 *
 */
@Document(collection = "components")
public class Component extends BaseModel {

	private String name; // must be unique to the application

	private String owner;

	private Map<CollectorType, List<CollectorItem>> collectorItems = new HashMap<>();

	public Component() {
	}

	public Component(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Map<CollectorType, List<CollectorItem>> getCollectorItems() {
		return collectorItems;
	}

	public void setCollectorItems(Map<CollectorType, List<CollectorItem>> collectorItems) {
		this.collectorItems = collectorItems;
	}

	public List<CollectorItem> getCollectorItems(CollectorType type) {
		return collectorItems != null && collectorItems.get(type) != null ? collectorItems.get(type)
				: new ArrayList<>();
	}

	public void addCollectorItem(CollectorType collectorType, CollectorItem collectorItem) {
		// Currently only one collectorItem per collectorType is supported
		if (collectorItems.get(collectorType) == null) {
			collectorItems.put(collectorType, Collections.singletonList(collectorItem));
		}
		else {
			List<CollectorItem> existing = new ArrayList<>(collectorItems.get(collectorType));
			if (isNewCollectorItem(existing, collectorItem)) {
				existing.add(collectorItem);
				collectorItems.put(collectorType, existing);
			}
		}
	}

	public void updateCollectorItem(CollectorType collectorType, CollectorItem collectorItem) {
		List<CollectorItem> existing = new ArrayList<>(collectorItems.get(collectorType));
		if (!isNewCollectorItem(existing, collectorItem)) {
			findCollectorItem(existing, collectorItem).setLastUpdated(collectorItem.getLastUpdated());
			collectorItems.put(collectorType, existing);
		}
	}

	private boolean isNewCollectorItem(List<CollectorItem> existing, CollectorItem item) {
		return existing.stream().noneMatch(ci -> Objects.equals(ci.getId(), item.getId()));
	}

	private CollectorItem findCollectorItem(List<CollectorItem> existing, CollectorItem item) {
		return existing.stream().filter(ci -> Objects.equals(ci.getId(), item.getId())).findFirst().orElse(null);
	}

	public CollectorItem getFirstCollectorItemForType(CollectorType type) {

		if (getCollectorItems().get(type) == null) {
			return null;
		}
		List<CollectorItem> collectorItems = new ArrayList<>(getCollectorItems().get(type));
		return collectorItems.get(0);
	}

	public CollectorItem getLastUpdatedCollectorItemForType(CollectorType type) {

		if (getCollectorItems().get(type) == null || getCollectorItems().get(type).isEmpty()) {
			return null;
		}
		List<CollectorItem> collectorItems = new ArrayList<>(getCollectorItems().get(type));
		return getLastUpdateItem(collectorItems);
	}

	private CollectorItem getLastUpdateItem(List<CollectorItem> collectorItems) {
		Comparator<CollectorItem> collectorItemComparator = Comparator.comparing(CollectorItem::getLastUpdated);
		collectorItems.sort(collectorItemComparator.reversed());
		return collectorItems.get(0);
	}

}
