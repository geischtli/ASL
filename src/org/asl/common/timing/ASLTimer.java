package org.asl.common.timing;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class ASLTimer {

	private final Logger logger;
	
	public ASLTimer() {
		logger = LogManager.getLogger(ASLTimer.class.getName());
	}
	
	public static ASLTimer create() {
		return new ASLTimer();
	}
	
	public void click(Timing timing, int clientId, int requestId) {
		logger.info("C:" + clientId + ",R:" + requestId + ",L:" + timing + ",T:" + System.nanoTime());
	}
	
}
