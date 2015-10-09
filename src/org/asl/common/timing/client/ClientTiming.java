package org.asl.common.timing.client;

public class ClientTiming {

	private long startRequest;
	private long sentRequest;
	private long readAnswer;
	private long processedAnswer;
	
	public ClientTiming() {
		this.startRequest = 0;
		this.sentRequest = 0;
		this.readAnswer = 0;
		this.processedAnswer = 0;
	}
	
	public static ClientTiming create() {
		return new ClientTiming();
	}
	
	public long getStartRequest() {
		return startRequest;
	}
	public void setStartRequest(long startRequest) {
		this.startRequest = startRequest;
	}
	public long getSentRequest() {
		return sentRequest;
	}
	public void setSentRequest(long sentRequest) {
		this.sentRequest = sentRequest;
	}
	public long getReadAnswer() {
		return readAnswer;
	}
	public void setReadAnswer(long readAnswer) {
		this.readAnswer = readAnswer;
	}
	public long getProcessedAnswer() {
		return processedAnswer;
	}
	public void setProcessedAnswer(long processedAnswer) {
		this.processedAnswer = processedAnswer;
	}
	
	public long getTotalTimeNanos() {
		return processedAnswer - startRequest;
	}
	
	public long getTotalTimeMillis() {
		return getTotalTimeNanos()/1000000;
	}
	
}
