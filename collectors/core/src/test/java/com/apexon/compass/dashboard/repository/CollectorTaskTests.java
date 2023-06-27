package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.collector.CollectorTask;
import com.apexon.compass.dashboard.model.Collector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CollectorTaskTests {

	private static final String COLLECTOR_NAME = "Test Collector";

	@Mock
	private TaskScheduler taskScheduler;

	@Mock
	private BaseCollectorRepository<Collector> baseCollectorRepository;

	private CollectorTask<Collector> task;

	@BeforeEach
	void init() {
		task = new TestCollectorTask();
	}

	@Test
	void run_collectorNotRegistered_savesNewCollector() {
		Collector c = new Collector();
		when(baseCollectorRepository.findByName(COLLECTOR_NAME)).thenReturn(null);
		when(baseCollectorRepository.save(any(Collector.class))).thenReturn(c);
		task.run();
		verify(baseCollectorRepository).save(any(Collector.class));
	}

	@Test
	void run_enabled() {
		Collector c = new Collector();
		c.setEnabled(true);
		long prevLastExecuted = c.getLastExecuted();
		when(baseCollectorRepository.findByName(COLLECTOR_NAME)).thenReturn(c);
		when(baseCollectorRepository.save(any(Collector.class))).thenReturn(c);
		task.run();

		assertThat(c.getLastExecuted(), greaterThan(prevLastExecuted));
		verify(baseCollectorRepository, times(1)).save(c);
	}

	@Test
	void run_disabled() {
		Collector c = new Collector();
		c.setEnabled(false);
		when(baseCollectorRepository.findByName(COLLECTOR_NAME)).thenReturn(c);
		when(baseCollectorRepository.save(any(Collector.class))).thenReturn(c);
		task.run();

		verify(baseCollectorRepository, never()).save(c);
	}

	@Test
	void onStartup() {
		Collector c = new Collector();
		c.setOnline(false);
		when(baseCollectorRepository.findByName(COLLECTOR_NAME)).thenReturn(c);
		task.onStartup();

		assertThat(c.isOnline(), is(true));
		verify(baseCollectorRepository, times(1)).save(c);
		verify(taskScheduler).schedule(any(TestCollectorTask.class), any(CronTrigger.class));
	}

	@Test
	void onShutdown() {
		Collector c = new Collector();
		c.setOnline(true);
		when(baseCollectorRepository.findByName(COLLECTOR_NAME)).thenReturn(c);

		task.onShutdown();

		assertThat(c.isOnline(), is(false));
		verify(baseCollectorRepository, times(1)).save(c);
	}

	private final class TestCollectorTask extends CollectorTask<Collector> {

		public TestCollectorTask() {
			super(taskScheduler, COLLECTOR_NAME);
		}

		@Override
		public Collector getCollector() {
			return new Collector();
		}

		@Override
		public BaseCollectorRepository<Collector> getCollectorRepository() {
			return baseCollectorRepository;
		}

		@Override
		public String getCron() {
			return "0 * * * * *";
		}

		@Override
		public void collect(Collector collector) {
		}

	}

}
