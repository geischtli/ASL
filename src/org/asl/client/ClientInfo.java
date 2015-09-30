package org.asl.client;

public class ClientInfo {
	
	private static int client_id;
	private static int queue_id;
	
	public static int getClientId() {
		return client_id;
	}
	
	public static void setClientId(int client_id) {
		ClientInfo.client_id = client_id; 
	}

	public static int getQueueId() {
		return queue_id;
	}

	public static void setQueueId(int queue_id) {
		ClientInfo.queue_id = queue_id;
	}
}