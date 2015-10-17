package org.asl.common.timing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TimeLogger {

	private BufferedWriter bw;
	
	public TimeLogger(String caller, int callerId, long startTime) {
		File file = new File(getFileLocation(caller, callerId));
		try {
			if (file.exists()) {
				file.delete();
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(String.valueOf(startTime));
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public String getFileLocation(String caller, int callerId) {
		return "logs/" + caller.toLowerCase() + "_" + callerId + ".log";
	}
	
	public static TimeLogger create(String caller, int callerId, long startTime) {
		return new TimeLogger(caller, callerId, startTime);
	}
	
	public void click(Timing timing, int clientId, int requestId, long callerStartTime) {
			setClick(timing, System.nanoTime(), clientId, requestId, callerStartTime);
	}
	
	public void setClick(Timing timing, long time, int clientId, int requestId, long callerStartTime) {
		try {
			synchronized(bw) {
				bw.write(clientId + " " + requestId + " " + timing.ordinal() + " " + (time - callerStartTime)/1000000);
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stopMyTimeLogger() {
		try {
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Closed time appender successfully");
	}
	
}