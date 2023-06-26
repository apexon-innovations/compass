package com.apexon.compass.dashboard.event;

import com.apexon.compass.dashboard.event.constants.sync.Reason;
import com.apexon.compass.dashboard.event.sync.SyncDashboard;
import com.apexon.compass.dashboard.event.sync.SyncException;
import com.apexon.compass.dashboard.model.relation.RelatedCollectorItem;
import com.apexon.compass.dashboard.repository.CollectorItemRepository;
import com.apexon.compass.dashboard.repository.CollectorRepository;
import com.apexon.compass.dashboard.repository.PipelineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;

@org.springframework.stereotype.Component
public class RelatedCollectorItemEventListener extends HygieiaMongoEventListener<RelatedCollectorItem> {

	private static final Logger LOG = LoggerFactory.getLogger(RelatedCollectorItemEventListener.class);

	private final SyncDashboard syncDashboard;

	@Autowired
	public RelatedCollectorItemEventListener(CollectorItemRepository collectorItemRepository,
			PipelineRepository pipelineRepository, CollectorRepository collectorRepository,
			SyncDashboard syncDashboard) {
		super(collectorItemRepository, pipelineRepository, collectorRepository);
		this.syncDashboard = syncDashboard;
	}

	@Override
	public void onAfterSave(AfterSaveEvent<RelatedCollectorItem> event) {
		RelatedCollectorItem relatedCollectorItem = event.getSource();
		try {
			syncDashboard.sync(relatedCollectorItem,
					!Reason.ARTIFACT_REASON.getAction().equalsIgnoreCase(relatedCollectorItem.getReason()));
		}
		catch (SyncException e) {
			LOG.error("Error processing related collector item. ID = " + relatedCollectorItem.getId() + ". Reason "
					+ e.getMessage());
		}
	}

}
