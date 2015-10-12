package org.asl.common.request.types.client;

import org.asl.client.ClientInfo;
import org.asl.common.request.Request;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.request.types.exceptions.HandshakeException;
import org.asl.middleware.database.dao.impl.ClientDAO;

public class HandshakeRequest extends Request {
	
	private static final long serialVersionUID = 106L;
	private int clientIdReturnedFromDB;
	
	public HandshakeRequest(int clientId, int requestId) {
		super(clientId, requestId);
		this.clientIdReturnedFromDB = 0;
		this.exception = new HandshakeException();
	}
	
	public int getClientIdReturnedFromDB() {
		return clientIdReturnedFromDB;
	}

	public void setClientIdReturnedFromDB(int clientIdReturnedFromDB) {
		this.clientIdReturnedFromDB = clientIdReturnedFromDB;
	}

	@Override
	public void processOnMiddleware() {
		try {
			setClientIdReturnedFromDB(ClientDAO.getClientDAO().registerClient(clientId, requestId));
		} catch (HandshakeException e) {
			setException(e);
		}
	}

	@Override
	public void processOnClient(ClientInfo ci) throws ASLException {
		if (!getException().carriesError()) {
			ci.setClientId(clientIdReturnedFromDB);
			System.out.println("Client got ID " + ci.getClientId());
		} else {
			throw getException();
		}
	}
}