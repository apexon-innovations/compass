package com.apexon.compass.dashboard.event;

import com.apexon.compass.dashboard.event.sync.SyncDashboard;
import com.apexon.compass.dashboard.model.*;
import com.apexon.compass.dashboard.repository.*;
import com.apexon.compass.dashboard.util.TestUtils;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuildEventListenerTest {

	@Mock
	private ComponentRepository componentRepository;

	@Mock
	private DashboardRepository dashboardRepository;

	@Mock
	private CollectorRepository collectorRepository;

	@Mock
	private CollectorItemRepository collectorItemRepository;

	@Mock
	private PipelineRepository pipelineRepository;

	@InjectMocks
	private BuildEventListener eventListener;

	@Mock
	private SyncDashboard syncDashboard;

	@Mock
	private CommitRepository commitRepository;

	private static final boolean HAS_BUILD_COLLECTOR = true;

	@Test
	void buildSaved_addedToPipeline() {
		Build build = TestUtils.createBuild();
		CollectorItem productCI = collectorItem();
		Dashboard dashboard = createDashboard(HAS_BUILD_COLLECTOR, productCI);
		Pipeline pipeline = new Pipeline();
		setupFindDashboards(build, dashboard);
		setupGetOrCreatePipeline(dashboard, pipeline, productCI);
		eventListener.onAfterSave(new AfterSaveEvent<>(build, null, ""));
		Map<String, EnvironmentStage> pipelineMap = pipeline.getEnvironmentStageMap();
		Assert.assertEquals(2, pipelineMap.get("Build").getCommits().size());
		verify(pipelineRepository).save(pipeline);
	}

	@Test
	void buildSaved_addedToPipeline_commitStage() {
		Build build = TestUtils.createBuild();
		CollectorItem productCI = collectorItem();
		Dashboard dashboard = createDashboard(HAS_BUILD_COLLECTOR, productCI);
		Pipeline pipeline = new Pipeline();
		pipeline.addCommit(PipelineStage.COMMIT.getName(), TestUtils.createPipelineCommit("scmRev3"));
		setupFindDashboards(build, dashboard);
		setupGetOrCreatePipeline(dashboard, pipeline, productCI);
		List<Commit> commits = new ArrayList<>();
		commits.add(TestUtils.createCommit("scmRev3", "http://github.com/scmurl"));
		when(commitRepository.findByScmRevisionNumber("scmRev3")).thenReturn(commits);
		eventListener.onAfterSave(new AfterSaveEvent<>(build, null, ""));
		Map<String, EnvironmentStage> pipelineMap = pipeline.getEnvironmentStageMap();
		Assert.assertEquals(3, pipelineMap.get("Build").getCommits().size());
		verify(pipelineRepository).save(pipeline);
	}

	private Dashboard createDashboard(boolean hasBuildCollector, CollectorItem productCI) {
		Component component = new Component();
		component.setId(ObjectId.get());
		component.addCollectorItem(CollectorType.Product, productCI);
		if (hasBuildCollector) {
			component.addCollectorItem(CollectorType.Build, collectorItem());
		}

		Application application = new Application("app", component);
		List<String> activeWidgets = new ArrayList<>();
		List<Owner> owners = new ArrayList<Owner>();
		owners.add(new Owner("owner", AuthType.STANDARD));
		Dashboard dashboard = new Dashboard("template", "title", application, owners, DashboardType.Team, "ASVTEST",
				"BAPTEST", activeWidgets, false, ScoreDisplayType.HEADER);
		dashboard.setId(ObjectId.get());
		return dashboard;
	}

	private void setupFindDashboards(Build build, Dashboard dashboard) {
		CollectorItem commitCollectorItem = new CollectorItem();
		List<Component> components = Collections.singletonList(dashboard.getApplication().getComponents().get(0));
		List<ObjectId> componentIds = components.stream().map(BaseModel::getId).collect(Collectors.toList());
		commitCollectorItem.setId(build.getCollectorItemId());
		when(collectorItemRepository.findById(build.getCollectorItemId())).thenReturn(Optional.of(commitCollectorItem));
		when(componentRepository.findByBuildCollectorItemId(commitCollectorItem.getId())).thenReturn(components);
		when(dashboardRepository.findByApplicationComponentIdsIn(componentIds))
			.thenReturn(Collections.singletonList(dashboard));
	}

	private void setupGetOrCreatePipeline(Dashboard dashboard, Pipeline pipeline, CollectorItem productCI) {
		Collector productCollector = new Collector();
		productCollector.setId(ObjectId.get());
		// when(collectorRepository.findByCollectorType(CollectorType.Product))
		// .thenReturn(Collections.singletonList(productCollector));
		when(pipelineRepository.findByCollectorItemId(productCI.getId())).thenReturn(pipeline);
	}

	private CollectorItem collectorItem() {
		CollectorItem item = new CollectorItem();
		item.setId(ObjectId.get());
		return item;
	}

}
