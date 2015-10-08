package org.asl.common.timer.middleware;

import java.util.ArrayList;
import java.util.List;

public class MiddlewareTimer {

	private List<MiddlewareTiming> timings;
	
	public MiddlewareTimer() {
		this.timings = new ArrayList<MiddlewareTiming>();
	}
	
	public static MiddlewareTimer create() {
		return new MiddlewareTimer();
	}
	
	public List<MiddlewareTiming> getTimings() {
		return timings;
	}
	
	public void setTimings(List<MiddlewareTiming> timings) {
		this.timings = timings;
	}

	public void click(MiddlewareTimings timing, int requestId) {
		switch (timing) {
			case ACCEPTED_CLIENT:
				timings.add(MiddlewareTiming.create());
				timings.get(requestId).setAcceptedClient(System.nanoTime());
				break;
			case EXECUTED_QUERY:
				timings.get(requestId).setExecutedQuery(System.nanoTime());
				break;
			case GOT_CONNECTION:
				timings.get(requestId).setGotConnection(System.nanoTime());
				break;
			case PACKED_REQUEST:
				timings.get(requestId).setPackedRequest(System.nanoTime());
				break;
			case PROCESSED_READ:
				timings.get(requestId).setProcessedRead(System.nanoTime());
				break;
			case READ_REQUEST:
				timings.get(requestId).setReadRequest(System.nanoTime());
				break;
			case WROTE_ANSWER:
				timings.get(requestId).setWroteAnswer(System.nanoTime());
				break;
			default:
				System.out.println("UNKOWN MIDDLEWARETIMING");
				break;
		}
	}
	
	public void printSingleRequestTimings(int requestId) {
		System.out.print("Request " + requestId + ": ");
		timings.get(requestId).printAllValues();
	}
	
}
