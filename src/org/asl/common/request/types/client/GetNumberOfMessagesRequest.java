package org.asl.common.request.types.client;

import org.asl.client.ClientInfo;
import org.asl.common.request.Request;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.request.types.exceptions.GetNumberOfMessagesException;
import org.asl.middleware.database.dao.impl.MessageDAO;

public class GetNumberOfMessagesRequest extends Request {

	private static final long serialVersionUID = 112L;
	private int numberOfMessages;
	
	public GetNumberOfMessagesRequest(int clientId, int requestId) {
		super(clientId, requestId);
		this.numberOfMessages = -1;
		this.exception = new GetNumberOfMessagesException();
	}
	
	public int getNumberOfMessages() {
		return numberOfMessages;
	}

	public void setNumberOfMessages(int numberOfMessages) {
		this.numberOfMessages = numberOfMessages;
	}
	
	@Override
	public void processOnMiddleware() {
		try {
			setNumberOfMessages(MessageDAO.getMessageDAO().getNumberOfMessages(clientId, requestId));
		} catch (GetNumberOfMessagesException e) {
			setException(e);
		}
	}

	@Override
	public void processOnClient(ClientInfo ci) throws ASLException {
		if (!getException().carriesError()) {
			ci.setNumberOfMessages(getNumberOfMessages());
		} else {
			throw getException();
		}
	}

}
