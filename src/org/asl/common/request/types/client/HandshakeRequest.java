package org.asl.common.request.types.client;

import org.asl.client.ClientInfo;
import org.asl.common.request.Request;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.request.types.exceptions.HandshakeException;
import org.asl.middleware.database.dao.impl.ClientDAO;

@SuppressWarnings("serial")
public class HandshakeRequest extends Request {
	
	private static final long serialVersionUID = 106L;
	private int client_id;
	
	public HandshakeRequest() {
		this.client_id = 0;
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