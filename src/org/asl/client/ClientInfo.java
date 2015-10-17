package org.asl.client;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.asl.common.timing.TimeLogger;

public class ClientInfo {
	
	private int clientId;
	
	private int deleteQueueId;
	private int readQueueId;
	private int sendQueueId;
	private List<Integer> clientsOnline;
	private List<Integer> queuesOnline;
	private int readFromSenderId;
	private int sendReceiverId;
	private int numberOfMessages;
	private String sendContext;
	private AtomicInteger requestId; // counter for globally unique request id tuple
	private TimeLogger myTimeLogger;
	private long startTime;
	
	public ClientInfo() {
		this.clientId = 0;
		this.deleteQueueId = 0;
		this.readFromSenderId = 0;
		this.sendQueueId = 0;
		this.clientsOnline = null;
		this.queuesOnline = null;
		this.readFromSenderId = 0;
		this.sendReceiverId = 0;
		this.numberOfMessages = -1;
		this.sendContext = "";
		this.requestId = new AtomicInteger(0);
		this.myTimeLogger = TimeLogger.create("CLIENT", this.clientId, System.nanoTime());
		this.startTime = -1;
	}
	
	public static ClientInfo create() {
		return new ClientInfo();
	}
	
	public int getClientId() {
		return clientId;
	}
	
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	
	public int getDeleteQueueId() {
		return deleteQueueId;
	}
	
	public void setDeleteQueueId(int deleteQueueId) {
		this.deleteQueueId = deleteQueueId;
	}
	
	public int getReadQueueId() {
		return readQueueId;
	}
	
	public void setReadQueueId(int readQueueId) {
		this.readQueueId = readQueueId;
	}
	
	public int getReadFromSenderId() {
		return readFromSenderId;
	}
	
	public void setReadFromSenderId(int readFromSenderId) {
		this.readFromSenderId = readFromSenderId;
	}
	
	public int getSendReceiverId() {
		return sendReceiverId;
	}
	
	public void setSendReceiverId(int sendReceiverId) {
		this.sendReceiverId = sendReceiverId;
	}
	
	public int getSendQueueId() {
		return sendQueueId;
	}
	
	public void setSendQueueId(int sendQueueId) {
		this.sendQueueId = sendQueueId;
	}
	
	public String getSendContentText() {
		return "This is a message from Client " + this.clientId;
	}

	public int getNumberOfMessages() {
		return numberOfMessages;
	}

	public void setNumberOfMessages(int numberOfMessages) {
		this.numberOfMessages = numberOfMessages;
	}

	public List<Integer> getClientsOnline() {
		return clientsOnline;
	}

	public void setClientsOnline(List<Integer> clientsOnline) {
		this.clientsOnline = clientsOnline;
	}

	public List<Integer> getQueuesOnline() {
		return queuesOnline;
	}

	public void setQueuesOnline(List<Integer> queuesOnline) {
		this.queuesOnline = queuesOnline;
	}
	
	public int getRequestId() {
		return requestId.get();
	}
	
	public void incrementRequestId() {
		requestId.incrementAndGet();
	}

	public String getSendContext() {
		return sendContext;
	}

	public void setSendContext(String sendContext) {
		this.sendContext = sendContext;
	}
	
	public void initTimeLogger() {
		setStartTime(System.nanoTime());
		myTimeLogger = TimeLogger.create("CLIENT", getClientId(), getStartTime()/1000000);
	}
	
	public TimeLogger getMyTimeLogger() {
		return myTimeLogger;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
}