package org.asl.common.timing;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.nio.charset.Charset;

public class TimeLogger {

	private Logger myLogger;
	private FileAppender myAppender;
	
	public TimeLogger(String caller, int callerId) {
		Layout<String> layout = PatternLayout.createLayout(
				"%m %ex%n", // pattern
				null, // config
				null, // replace regex
				Charset.forName("UTF-8"), // charset
				true, // write exceptions
				false,
				caller + "LOG " + callerId + " START", // header
				caller + "LOG " + callerId + " START" // footer
			);
		myAppender = FileAppender.createAppender(
				caller.toLowerCase() + "_logs/client_" + callerId, // filename
				"false", // append
				"false", // locking
				caller.toUpperCase() + "APPENDER " + callerId, // appender name
				"true", // immediateFlush
				"true", // log errors, else propagate to caller
				"true", // bufferedIo
				"8192", // bufferSize
				layout, // layout
				null, // filter
				"false", // advertise
				"", // advertiseURI
				null // config
			);
		myLogger = org.apache.logging.log4j.LogManager.getLogger("Client" + callerId + "Logger");
		org.apache.logging.log4j.core.Logger coreLogger = (org.apache.logging.log4j.core.Logger)myLogger;
		coreLogger.addAppender(myAppender);
	}
	
	
	public void click(Timing timing, int clientId, int requestId) {
		myLogger.info("C:" + clientId + ",R:" + requestId + ",L:" + timing + ",T:" + System.nanoTime());
	}
	
	public void setClick(Timing timing, long time, int clientId, int requestId) {
		myLogger.info("C:" + clientId + ",R:" + requestId + ",L:" + timing + ",T:" + time);
	}
	
}