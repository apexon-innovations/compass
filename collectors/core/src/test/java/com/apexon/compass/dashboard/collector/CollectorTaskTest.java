package com.apexon.compass.dashboard.collector;

import com.apexon.compass.dashboard.model.Collector;
import com.apexon.compass.dashboard.model.RepoBranch;
import com.apexon.compass.dashboard.repository.BaseCollectorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.TaskScheduler;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CollectorTaskTest {

	@Mock
	private TaskScheduler taskScheduler;

	private static final String COLLECTOR_NAME = "Test Collector";

	private CollectorTask<Collector> collector;

	@BeforeEach
	void init() {
		collector = new CollectorTaskTest.TestCollectorTask();
		System.out.println(collector);
	}

	@Test
	void throttleRequestsTest_ratelimit_exceeded() {
		long startTime = System.currentTimeMillis() - 500;
		int requestCount = 5;
		long waitTime = 500;
		int requestRateLimit = 3;
		long requestRateLimitTimeWindow = 1000;

		CollectorTask<Collector> collectorSpy = Mockito.spy(collector);
		boolean result = collectorSpy.throttleRequests(startTime, requestCount, waitTime, requestRateLimit,
				requestRateLimitTimeWindow);
		assertTrue(result);
		verify(collectorSpy, times(1)).sleep(Mockito.anyLong());
	}

	@Test
	void throttleRequestsTest_ratelimit_not_exceeded() {
		long startTime = System.currentTimeMillis() - 500;
		int requestCount = 2;
		long waitTime = 500;
		int requestRateLimit = 3;
		long requestRateLimitTimeWindow = 1000;

		CollectorTask<Collector> collectorSpy = Mockito.spy(collector);
		boolean result = collectorSpy.throttleRequests(startTime, requestCount, waitTime, requestRateLimit,
				requestRateLimitTimeWindow);
		assertFalse(result);
		verify(collectorSpy, times(0)).sleep(Mockito.anyLong());
	}

	@Test
	void throttleRequestsTest_ratelimit_exceeded_with_timeWindow_greaterThan_rateLimitTimeWindow() {
		long startTime = System.currentTimeMillis() - 2000;
		int requestCount = 5;
		long waitTime = 500;
		int requestRateLimit = 3;
		long requestRateLimitTimeWindow = 1000;

		CollectorTask<Collector> collectorSpy = Mockito.spy(collector);
		boolean result = collectorSpy.throttleRequests(startTime, requestCount, waitTime, requestRateLimit,
				requestRateLimitTimeWindow);
		assertTrue(result);
		verify(collectorSpy, times(0)).sleep(Mockito.anyLong());
	}

	@Test
	void testRepoBranch() {

		String branch = "master";
		String url0 = "git+https://gh.pages.com/org/repo.git";
		String url1 = "git@gh.pages.com:org/repo";
		RepoBranch rp0 = new RepoBranch(url0, branch, RepoBranch.RepoType.GIT);
		RepoBranch rp1 = new RepoBranch(url1, branch, RepoBranch.RepoType.GIT);
		boolean result = rp0.equals(rp1);
		assertTrue(result);
	}

	@Test
	void testRepoBranchSSH() {

		String branch = "master";
		String url0 = "ssh://gh.pages.com/org/repo.git";
		String url1 = "git@gh.pages.com:org/repo";
		RepoBranch rp0 = new RepoBranch(url0, branch, RepoBranch.RepoType.GIT);
		RepoBranch rp1 = new RepoBranch(url1, branch, RepoBranch.RepoType.GIT);
		boolean result = rp0.equals(rp1);
		assertTrue(result);
	}

	@Test
	void testRepoBranchNonZeroPort() {

		String branch = "master";
		String url0 = "https://gh.pages.com:8087/org/repo.git";
		String url1 = "git@gh.pages.com:org/repo";
		RepoBranch rp0 = new RepoBranch(url0, branch, RepoBranch.RepoType.GIT);
		RepoBranch rp1 = new RepoBranch(url1, branch, RepoBranch.RepoType.GIT);
		boolean result = rp0.equals(rp1);
		assertTrue(result);
	}

	@Test
	void testRepoBranchSpecialCharacter() {

		String branch = "master";
		String url0 = "https://gh.pages.com/org/repo-1.git";
		String url1 = "git@gh.pages.com:org/repo-1";
		RepoBranch rp0 = new RepoBranch(url0, branch, RepoBranch.RepoType.GIT);
		RepoBranch rp1 = new RepoBranch(url1, branch, RepoBranch.RepoType.GIT);
		boolean result = rp0.equals(rp1);
		assertTrue(result);
	}

	@Test
	void testRepoBranchWhiteSpace() {

		String branch = "master";
		String url0 = "https://gh.pages.com/org one/repo-1.git";
		String url1 = "git@gh.pages.com:org one/repo-1";
		RepoBranch rp0 = new RepoBranch(url0, branch, RepoBranch.RepoType.GIT);
		RepoBranch rp1 = new RepoBranch(url1, branch, RepoBranch.RepoType.GIT);
		boolean result = rp0.equals(rp1);
		assertTrue(result);
	}

	private class TestCollectorTask extends CollectorTask<Collector> {

		public TestCollectorTask() {
			super(taskScheduler, COLLECTOR_NAME);
		}

		@Override
		public Collector getCollector() {
			return new Collector();
		}

		@Override
		public BaseCollectorRepository<Collector> getCollectorRepository() {
			return null;
		}

		@Override
		public String getCron() {
			return null;
		}

		@Override
		public void collect(Collector collector) {
		}

	}

}