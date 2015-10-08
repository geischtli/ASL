package org.asl.common.request.types.client;

import org.asl.client.ClientInfo;
import org.asl.common.request.Request;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.request.types.exceptions.HandshakeException;
import org.asl.common.timer.middleware.MiddlewareTimer;
import org.asl.middleware.database.dao.impl.ClientDAO;

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
	public void processOnMiddleware(MiddlewareTimer timer, int reqCount) {
		try {
			setClientId(ClientDAO.getClientDAO().registerClient(timer, reqCount));
		} catch (HandshakeException e) {
			setException(e);
		}
	}

	@Override
	public void processOnClient(ClientInfo ci) throws ASLException {
		if (!getException().carriesError()) {
			ci.setClientId(client_id);
			System.out.println("Client got ID " + ci.getClientId());
		} else {
			throw getException();
		}
	}
}