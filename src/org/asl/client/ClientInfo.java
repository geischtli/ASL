package org.asl.client;

public class ClientInfo {
	
	private static int client_id;
	
	public static int getClientId() {
		return client_id;
	}
	
	public static void setClientId(int client_id) {
		ClientInfo.client_id = client_id; 
	}
}
