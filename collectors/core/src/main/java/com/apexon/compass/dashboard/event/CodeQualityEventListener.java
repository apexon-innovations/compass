package com.apexon.compass.dashboard.event;

import com.apexon.compass.dashboard.event.sync.SyncDashboard;
import com.apexon.compass.dashboard.model.CodeQuality;
import com.apexon.compass.dashboard.repository.CollectorItemRepository;
import com.apexon.compass.dashboard.repository.CollectorRepository;
import com.apexon.compass.dashboard.repository.PipelineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;

@org.springframework.stereotype.Component
public class CodeQualityEventListener extends HygieiaMongoEventListener<CodeQuality> {

	private final SyncDashboard syncDashboard;

	@Autowired
	public CodeQualityEventListener(CollectorItemRepository collectorItemRepository,
			PipelineRepository pipelineRepository, CollectorRepository collectorRepository,
			SyncDashboard syncDashboard) {
		super(collectorItemRepository, pipelineRepository, collectorRepository);
		this.syncDashboard = syncDashboard;
	}

	@Override
	public void onAfterSave(AfterSaveEvent<CodeQuality> event) {
		CodeQuality codeQuality = event.getSource();
		syncDashboard.sync(codeQuality);
	}

}
