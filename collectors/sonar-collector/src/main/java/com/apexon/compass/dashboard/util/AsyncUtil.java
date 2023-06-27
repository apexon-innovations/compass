package com.apexon.compass.dashboard.util;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncUtil {

	@Async("threadPoolExecutor")
	public void close() {
		try {
			Thread.sleep(20000);
			System.exit(0);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
