package com.apexon.compass.dashboard.event;

import com.apexon.compass.dashboard.model.*;
import com.apexon.compass.dashboard.repository.*;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;

import java.util.*;
import java.util.stream.Collectors;

import static com.apexon.compass.dashboard.util.TestUtils.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnvironmentComponentEventListenerTest {

	@Mock
	private ComponentRepository componentRepository;

	@Mock
	private DashboardRepository dashboardRepository;

	@Mock
	private CollectorRepository collectorRepository;

	@Mock
	private CollectorItemRepository collectorItemRepository;

	@Mock
	private BinaryArtifactRepository binaryArtifactRepository;

	@Mock
	private PipelineRepository pipelineRepository;

	@InjectMocks
	private EnvironmentComponentEventListener eventListener;

	@Mock
	private CommitRepository commitRepository;

	private static final boolean HAS_BUILD_COLLECTOR = true;

	@Test
	void commit_in_environment_stage_addedToPipeline() {
		EnvironmentComponent environmentComponent = createEnvironmentComponent();
		Dashboard dashboard = createDashboard(HAS_BUILD_COLLECTOR);
		setupFindDashboards(environmentComponent, dashboard);
		Pipeline pipeline = getPipeline();
		setupGetOrCreatePipeline(dashboard, pipeline);
		List<Commit> commits = new ArrayList<>();
		commits.add(createCommit("scmRev3", "http://github.com/scmurl"));
		List<BinaryArtifact> binaryArtifactList = new ArrayList<>();
		binaryArtifactList.add(getBinaryArtifact());
		when(binaryArtifactRepository.findByArtifactNameAndArtifactExtensionAndTimestampGreaterThan("hygieia-2.0.5",
				"jar", 374268428L))
			.thenReturn(binaryArtifactList);
		when(binaryArtifactRepository.findByArtifactNameAndArtifactExtensionAndTimestampGreaterThan("hygieia-2.0.5.jar",
				null, 374268428L))
			.thenReturn(binaryArtifactList);
		when(commitRepository.findByScmRevisionNumber("scmRev3")).thenReturn(commits);
		eventListener.onAfterSave(new AfterSaveEvent<>(environmentComponent, null, ""));
		Map<String, EnvironmentStage> pipelineMap = pipeline.getEnvironmentStageMap();
		Assertions.assertEquals(3, pipelineMap.get("DEV").getCommits().size());
		verify(pipelineRepository, times(3)).save(pipeline);
	}

	private Pipeline getPipeline() {
		Pipeline pipeline = new Pipeline();
		pipeline.addCommit(PipelineStage.COMMIT.getName(), createPipelineCommit("scmRev3"));
		EnvironmentStage environmentStage = new EnvironmentStage();
		environmentStage.setLastArtifact(getBinaryArtifact());
		pipeline.getEnvironmentStageMap().put("DEV", environmentStage);
		return pipeline;
	}

	private EnvironmentComponent createEnvironmentComponent() {
		EnvironmentComponent environmentComponent = new EnvironmentComponent();
		environmentComponent.setDeployed(true);
		environmentComponent.setEnvironmentName("DEV");
		environmentComponent.setComponentName("hygieia-2.0.5.jar");
		return environmentComponent;
	}

	private BinaryArtifact getBinaryArtifact() {
		BinaryArtifact binaryArtifact = new BinaryArtifact();
		binaryArtifact.setTimestamp(374268428);
		binaryArtifact.setBuildInfos(List.of(createBuild()));
		return binaryArtifact;
	}

	private Dashboard createDashboard(boolean hasBuildCollector) {
		Component component = new Component();
		component.setId(ObjectId.get());
		component.addCollectorItem(CollectorType.Product, collectorItem());
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

	private void setupFindDashboards(EnvironmentComponent environmentComponent, Dashboard dashboard) {
		CollectorItem commitCollectorItem = new CollectorItem();

		List<Component> components = Collections.singletonList(dashboard.getApplication().getComponents().get(0));
		List<ObjectId> componentIds = components.stream().map(BaseModel::getId).collect(Collectors.toList());
		commitCollectorItem.setId(environmentComponent.getCollectorItemId());
		when(collectorItemRepository.findById(environmentComponent.getCollectorItemId()))
			.thenReturn(Optional.of(commitCollectorItem));
		when(componentRepository.findByDeployCollectorItemId(commitCollectorItem.getId())).thenReturn(components);
		when(dashboardRepository.findByApplicationComponentIdsIn(componentIds))
			.thenReturn(Collections.singletonList(dashboard));
	}

	private void setupGetOrCreatePipeline(Dashboard dashboard, Pipeline pipeline) {
		Collector productCollector = new Collector();
		productCollector.setId(ObjectId.get());
		CollectorItem teamDashboardCI = collectorItem();

		when(collectorRepository.findByCollectorType(CollectorType.Product))
			.thenReturn(Collections.singletonList(productCollector));
		when(collectorItemRepository.findTeamDashboardCollectorItemsByCollectorIdAndDashboardId(
				productCollector.getId(), dashboard.getId().toString()))
			.thenReturn(teamDashboardCI);
		when(pipelineRepository.findByCollectorItemId(teamDashboardCI.getId())).thenReturn(pipeline);
	}

	private CollectorItem collectorItem() {
		CollectorItem item = new CollectorItem();
		item.setId(ObjectId.get());
		return item;
	}

}
