package org.asl.common.message.types.client;


import org.asl.client.ClientInfo;
import org.asl.common.message.Message;
import org.asl.common.message.types.exceptions.ASLException;
import org.asl.common.message.types.exceptions.CreateQueueException;
import org.asl.common.message.types.exceptions.HandshakeException;
import org.asl.middleware.database.SqlHandler;
import org.asl.middleware.database.dao.impl.ClientDAO;

@SuppressWarnings("serial")
public class HandshakeMessage extends Message {
	
	private int client_id;
	
	public HandshakeMessage(int client_id) {
		this.client_id = client_id;
		this.exception = new HandshakeException();
	}
	
	public int getClientId() {
		return client_id;
	}

	public void setClientId(int client_id) {
		this.client_id = client_id;
	}

	@Override
	public void processOnMiddleware() {
		try {
			setClientId(ClientDAO.getClientDAO().registerClient());
		} catch (HandshakeException e) {
			setException(e);
		}
	}

	@Override
	public void processOnClient() throws ASLException {
		if (!getException().carriesError()) {
			ClientInfo.setClientId(client_id);
			System.out.println("Client got ID " + ClientInfo.getClientId());
		} else {
			throw getException();
		}
	}
}