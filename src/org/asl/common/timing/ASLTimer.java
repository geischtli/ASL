package org.asl.common.timing;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class ASLTimer {

	private final Logger Logger = LogManager.getLogger(ASLTimer.class.getName());
	
	public ASLTimer() {
//		Configurator.initialize(null, "log_properties_client.xml");
		Logger.info("testing log4j2 with disruptor");
	}
	
	public static ASLTimer create() {
		return new ASLTimer();
	}
	
	public void click(Timing timing) {
		//logger.info("clicked");
	}
	
}
