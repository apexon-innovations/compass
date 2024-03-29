package com.apexon.compass.dashboard.collector;

import com.apexon.compass.dashboard.model.Collector;
import com.apexon.compass.dashboard.model.CollectorItem;
import com.apexon.compass.dashboard.repository.BaseCollectorRepository;
import com.google.common.base.Strings;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for Collector task implementation which provides subclasses with the
 * following:
 * <p>
 * <ol>
 * <li>Creates a Collector instance the first time the collector runs.</li>
 * <li>Uses TaskScheduler to schedule the job based on the provided cron when the process
 * starts.</li>
 * <li>Saves the last execution time on the collector when the collection run
 * finishes.</li>
 * <li>Sets the collector online/offline when the collector process starts/stops</li>
 * </ol>
 *
 * @param <T> Class that extends Collector
 */
@Component
public abstract class CollectorTask<T extends Collector> implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(CollectorTask.class);

	private final TaskScheduler taskScheduler;

	private final String collectorName;

	@Autowired
	protected CollectorTask(TaskScheduler taskScheduler, String collectorName) {
		this.taskScheduler = taskScheduler;
		this.collectorName = collectorName;
	}

	@Override
	public final void run() {
		LOGGER.info("Getting Collector: {}", collectorName);
		T collector = getCollectorRepository().findByName(collectorName);
		if (collector == null) {
			// Register new collector
			collector = getCollectorRepository().save(getCollector());
		}
		else {
			// In case the collector options changed via collectors properties setup.
			// We want to keep the existing collectors ID same as it ties to collector
			// items.
			T newCollector = getCollector();
			newCollector.setId(collector.getId());
			newCollector.setEnabled(collector.isEnabled());
			newCollector.setCollectorType(collector.getCollectorType());
			newCollector.setLastExecuted(collector.getLastExecuted());
			newCollector.setName(collector.getName());
			collector = getCollectorRepository().save(newCollector);
		}

		if (collector.isEnabled()) {
			LOGGER.info("Starting Collector={}", collectorName);
			long start = System.currentTimeMillis();
			if (CollectionUtils.isEmpty(getSelectedCollectorItems())) {
				collect(collector);
			}
			else {
				collect(collector, getSelectedCollectorItems());
			}
			long count = collector.getLastExecutionRecordCount();
			long end = System.currentTimeMillis();
			long duration = end - start;
			LOGGER.info("Finished running collector_name={} collector_run_duration=" + duration / 1000
					+ " collector_items_count=" + count, collectorName);

			// Update lastUpdate timestamp in Collector
			collector.setLastExecuted(end);
			collector.setLastExecutedSeconds(duration / 1000);
			getCollectorRepository().save(collector);
		}
		else {
			LOGGER.info("Collector is disabled, collector_name={}", collectorName);
		}
	}

	@PostConstruct
	public void onStartup() {
		taskScheduler.schedule(this, new CronTrigger(getCron()));
		setOnline(true);
	}

	@PreDestroy
	public void onShutdown() {
		setOnline(false);
	}

	public abstract T getCollector();

	public abstract BaseCollectorRepository<T> getCollectorRepository();

	public abstract String getCron();

	public abstract void collect(T collector);

	// default implementation that needs to be overridden in the collector.
	public void collect(T collector, List<CollectorItem> collectorItems) {
	}

	public List<CollectorItem> getSelectedCollectorItems() {
		return new ArrayList<>();
	}

	private void setOnline(boolean online) {
		T collector = getCollectorRepository().findByName(collectorName);
		if (collector != null) {
			collector.setOnline(online);
			getCollectorRepository().save(collector);
		}
	}

	protected boolean throttleRequests(long startTime, int requestCount, long waitTime, int requestRateLimit,
			long requestRateLimitTimeWindow) {
		boolean result = false;
		// Record Current Time
		long currentTime = System.currentTimeMillis();
		// Time Elapsed
		long timeElapsed = currentTime - startTime;
		if (requestCount >= requestRateLimit) {
			result = true;
			if (timeElapsed <= requestRateLimitTimeWindow) {
				long timeToWait = (timeElapsed < requestRateLimitTimeWindow)
						? ((requestRateLimitTimeWindow - timeElapsed) + waitTime) : waitTime;

				LOGGER.debug("Rates limit exceeded: rate_limit_time_elapsed=" + timeElapsed + " current_rate_count="
						+ requestCount + " waiting for " + timeToWait + " milliseconds");
				sleep(timeToWait);
			}
		}
		return result;
	}

	protected void sleep(long timeToWait) {
		try {
			Thread.sleep(timeToWait);
		}
		catch (InterruptedException ie) {
			LOGGER.error("Thread Interrupted ", ie);
		}
	}

	protected void log(String marker, long start) {
		log(marker, start, null);
	}

	protected void log(String text, long start, Integer count) {
		long end = System.currentTimeMillis();
		String elapsed = ((end - start) / 1000) + "s";
		String token2 = "";
		String token3;
		if (count == null) {
			token3 = Strings.padStart(" " + elapsed, 35 - text.length(), ' ');
		}
		else {
			token2 = Strings.padStart(" " + count.toString(), 25 - text.length(), ' ');
			token3 = Strings.padStart(" " + elapsed, 10, ' ');
		}
		LOGGER.info(text + token2 + token3);
	}

	protected void log(String message) {
		LOGGER.info(message);
	}

	@Deprecated
	protected void logBanner(String instanceUrl) {
		LOGGER.info("-----------------------------------");
		LOGGER.info(instanceUrl);
		LOGGER.info("-----------------------------------");
	}

}
