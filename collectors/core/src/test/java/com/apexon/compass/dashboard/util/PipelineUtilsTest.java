package com.apexon.compass.dashboard.util;

import com.apexon.compass.dashboard.model.*;
import com.apexon.compass.dashboard.repository.CommitRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.apexon.compass.dashboard.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by syq410 on 2/23/17.
 */
@ExtendWith(MockitoExtension.class)
class PipelineUtilsTest {

	private static final ObjectId DASHBOARD_ID = new ObjectId();

	@Mock
	private CommitRepository commitRepository;

	@Test
	void testOrderForStages() {
		Map<String, String> ordermap = PipelineUtils.getOrderForStages(setupDashboard());
		assertEquals("Commit", ordermap.get("0"));
		assertEquals("Build", ordermap.get("1"));
		assertEquals("dev", ordermap.get("2"));
		assertEquals("qa", ordermap.get("3"));
		assertEquals("int", ordermap.get("4"));

	}

	private Dashboard setupDashboard() {
		ObjectId configItemAppId = new ObjectId();
		ObjectId configItemComponetId = new ObjectId();
		List<String> activeWidgets = new ArrayList<>();
		List<Owner> owners = new ArrayList<Owner>();
		owners.add(new Owner("owner", AuthType.STANDARD));
		Dashboard rt = new Dashboard("Capone", "hygieia", new Application("hygieia", new Component()), owners,
				DashboardType.Team, "ASVTEST", "BAPTEST", activeWidgets, false, ScoreDisplayType.HEADER);

		Widget pipelineWidget = new Widget();
		pipelineWidget.setName("pipeline");
		Map<String, String> mappings = new HashMap<>();
		mappings.put("dev", "DEV");
		mappings.put("qa", "QA");
		mappings.put("int", "INT");
		pipelineWidget.getOptions().put("mappings", mappings);

		Map<String, String> order = new HashMap<>();
		order.put("0", "dev");
		order.put("1", "qa");
		order.put("2", "int");
		pipelineWidget.getOptions().put("order", order);

		rt.getWidgets().add(pipelineWidget);

		rt.setId(DASHBOARD_ID);

		return rt;
	}

	@Test
	void test_processFailedBuilds() {
		Build successBuild = createBuild();
		Pipeline pipeline = getPipeline(successBuild.getCollectorItemId());
		PipelineUtils.processPreviousFailedBuilds(successBuild, pipeline);
		assertEquals(0, pipeline.getFailedBuilds().size());
	}

	@Test
	void test_isMoveCommitToBuild() {

		List<Commit> commits = new ArrayList<>();
		commits.add(createCommit("scmRev1", "http://github.com/scmurl"));
		Mockito.when(commitRepository.findByScmRevisionNumber("scmRev1")).thenReturn(commits);
		assertTrue(PipelineUtils.isMoveCommitToBuild(createBuild(), getScm("scmRev1"), commitRepository));
	}

	@Test
	void test_isMoveCommitToBuild_false() {

		List<Commit> commits = new ArrayList<>();
		commits.add(createCommit("scmRev1", "http://github.com/scmurl1"));
		Mockito.when(commitRepository.findByScmRevisionNumber("scmRev1")).thenReturn(commits);
		assertFalse(PipelineUtils.isMoveCommitToBuild(createBuild(), getScm("scmRev1"), commitRepository));
	}

}
