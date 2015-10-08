package org.asl.client;

public class ClientInfo {
	
	private int clientId;
	
	private int deleteQueueId;
	private int readQueueId;
	private int sendQueueId;
	
	private int readFromSenderId;
	private int sendReceiverId;
	
	public ClientInfo() {
		this.clientId = 0;
		this.deleteQueueId = 0;
		this.readFromSenderId = 0;
		this.sendQueueId = 0;
		this.readFromSenderId = 0;
		this.sendReceiverId = 0;
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
	
}