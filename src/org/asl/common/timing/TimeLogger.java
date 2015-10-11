package org.asl.common.timing;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class TimeLogger {

	private final Logger logger;
	
	public TimeLogger() {
		logger = LogManager.getLogger(TimeLogger.class.getName());
	}
	
	public static TimeLogger create() {
		return new TimeLogger();
	}
	
	public void click(Timing timing, int clientId, int requestId) {
		logger.info("C:" + clientId + ",R:" + requestId + ",L:" + timing + ",T:" + System.nanoTime());
	}
	
}
