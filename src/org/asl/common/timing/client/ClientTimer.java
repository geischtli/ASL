package org.asl.common.timing.client;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClientTimer {

	private int numRequests;
	private int requestCounter;
	private List<ClientTiming> timings;
	
	public ClientTimer() {
		this.numRequests = 0;
		this.requestCounter = 0;
		timings = null;
	}
	
	public static ClientTimer create() {
		return new ClientTimer();
	}
	
	public void setup(int numRequests) {
		this.numRequests = numRequests;
		this.requestCounter = 0;
		this.timings = new ArrayList<ClientTiming>(numRequests);
		this.timings = Stream.generate(ClientTiming::create).limit(numRequests).collect(Collectors.toList());
	}
	
	public int getNumRequests() {
		return numRequests;
	}
	
	public void setNumRequests(int numRequests) {
		this.numRequests = numRequests;
	}
	
	public List<ClientTiming> getTimings() {
		return timings;
	}
	
	public void setTimings(List<ClientTiming> timings) {
		this.timings = timings;
	}
	
	public void click(ClientTimings timing) {
		switch (timing) {
			case START_REQUEST:
				try {
				//timings.add(ClientTiming.create());
				timings.get(requestCounter).setStartRequest(System.nanoTime());
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case SENT_REQUEST:
				timings.get(requestCounter).setSentRequest(System.nanoTime());
				break;
			case READ_ANSWER:
				timings.get(requestCounter).setReadAnswer(System.nanoTime());
				break;
			case PROCESSED_ANSWER:
				timings.get(requestCounter).setProcessedAnswer(System.nanoTime());
				requestCounter++;
				break;	
			default:
				System.out.println("UNKNOWN CLIENTTIMINGS ENUM");
				break;
		}
	}
	
	public void printTotalTimePerRequest() {
		for (ClientTiming t : timings) {
			System.out.println(t.getTotalTimeNanos() + " ns");
		}
	}

	
}
