package org.asl.client;

public class ClientInfo {
	
	private static int clientId;
	
	private static int deleteQueueId;
	private static int readQueueId;
	private static int sendQueueId;
	
	private static int readFromSenderId;
	private static int sendReceiverId;
	
	public static int getClientId() {
		return clientId;
	}
	
	public static void setClientId(int clientId) {
		ClientInfo.clientId = clientId;
	}
	
	public static int getDeleteQueueId() {
		return deleteQueueId;
	}
	
	public static void setDeleteQueueId(int deleteQueueId) {
		ClientInfo.deleteQueueId = deleteQueueId;
	}
	
	public static int getReadQueueId() {
		return readQueueId;
	}
	
	public static void setReadQueueId(int readQueueId) {
		ClientInfo.readQueueId = readQueueId;
	}
	
	public static int getReadFromSenderId() {
		return readFromSenderId;
	}
	
	public static void setReadFromSenderId(int readFromSenderId) {
		ClientInfo.readFromSenderId = readFromSenderId;
	}
	
	public static int getSendReceiverId() {
		return sendReceiverId;
	}
	
	public static void setSendReceiverId(int sendReceiverId) {
		ClientInfo.sendReceiverId = sendReceiverId;
	}
	
	public static int getSendQueueId() {
		return sendQueueId;
	}
	
	public static void setSendQueueId(int sendQueueId) {
		ClientInfo.sendQueueId = sendQueueId;
	}
	
	public static String getSendContentText() {
		return "This is a message from Client " + ClientInfo.clientId;
	}
	
}