package org.asl.common.timer.middleware;

public class MiddlewareTiming {

	private long acceptedClient;
	private long readRequest;
	private long processedRead;
	private long gotConnection;
	private long executedQuery;
	private long packedRequest;
	private long wroteAnswer;
	
	public MiddlewareTiming() {
		this.acceptedClient = 0;
		this.readRequest = 0;
		this.processedRead = 0;
		this.gotConnection = 0;
		this.executedQuery = 0;
		this.packedRequest = 0;
		this.wroteAnswer = 0;
	}
	
	public static MiddlewareTiming create() {
		return new MiddlewareTiming();
	}
	
	public long getAcceptedClient() {
		return acceptedClient;
	}
	
	public void setAcceptedClient(long acceptedClient) {
		this.acceptedClient = acceptedClient;
	}
	
	public long getReadRequest() {
		return readRequest;
	}
	
	public void setReadRequest(long readRequest) {
		this.readRequest = readRequest;
	}
	
	public long getProcessedRead() {
		return processedRead;
	}
	
	public void setProcessedRead(long processedRead) {
		this.processedRead = processedRead;
	}
	
	public long getGotConnection() {
		return gotConnection;
	}
	
	public void setGotConnection(long gotConnection) {
		this.gotConnection = gotConnection;
	}
	
	public long getExecutedQuery() {
		return executedQuery;
	}
	
	public void setExecutedQuery(long executedQuery) {
		this.executedQuery = executedQuery;
	}
	
	public long getPackedRequest() {
		return packedRequest;
	}
	
	public void setPackedRequest(long packedRequest) {
		this.packedRequest = packedRequest;
	}
	
	public long getWroteAnswer() {
		return wroteAnswer;
	}
	
	public void setWroteAnswer(long wroteAnswer) {
		this.wroteAnswer = wroteAnswer;
	}
	
	public void printAllValues() {
		System.out.println(
				(acceptedClient-acceptedClient)/1000000 + "/" +
				(readRequest-acceptedClient)/1000000 + "/" +
				(processedRead-readRequest)/1000000 + "/" +
				(gotConnection-processedRead)/1000000 + "/" +
				(executedQuery-gotConnection)/1000000 + "/" +
				(packedRequest-executedQuery)/1000000 + "/" +
				(wroteAnswer-packedRequest)/1000000
			);
	}
	
}
