package org.asl.common.message.types.client;


import org.asl.client.ClientInfo;
import org.asl.common.message.Message;
import org.asl.middleware.database.SqlHandler;

@SuppressWarnings("serial")
public class HandshakeMessage extends Message {
	
	private int client_id;
	
	public HandshakeMessage(int client_id) {
		this.client_id = client_id;
	}
	
	public int getClientId() {
		return client_id;
	}

	public void setClientId(int client_id) {
		this.client_id = client_id;
	}

	@Override
	public void processOnMiddleware() {
				
	}

	@Override
	public void processOnClient() {
		ClientInfo.setClientId(client_id);
	}
}