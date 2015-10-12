package org.asl.common.timing;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class TimeLogger {

	private static Logger logger = LogManager.getLogger(TimeLogger.class.getName());
	
	public static void click(Timing timing, int clientId, int requestId) {
		logger.info("C:" + clientId + ",R:" + requestId + ",L:" + timing + ",T:" + System.nanoTime());
	}
	
}
