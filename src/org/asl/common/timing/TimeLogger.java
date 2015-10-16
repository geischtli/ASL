package org.asl.common.timing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TimeLogger {

	private BufferedWriter bw;
	
	public TimeLogger(String caller, int callerId) {
		File file = new File(getFileLocation(caller, callerId));
		try {
			if (file.exists()) {
				file.delete();
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public String getFileLocation(String caller, int callerId) {
		return caller.toLowerCase() + "_logs/" + caller.toLowerCase() + "_" + callerId + ".log"; 
	}
	
	public static TimeLogger create(String caller, int callerId) {
		return new TimeLogger(caller, callerId);
	}
	
	public void click(Timing timing, int clientId, int requestId) {
		try {
			bw.write("C:" + clientId + ",R:" + requestId + ",L:" + timing + ",T:" + System.nanoTime());
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setClick(Timing timing, long time, int clientId, int requestId) {
		try {
			bw.write("C:" + clientId + ",R:" + requestId + ",L:" + timing + ",T:" + time + "\n");
			bw.newLine();
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