package org.asl.common.request.types.client;

import org.asl.common.request.Request;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.request.types.exceptions.ReadMessageFromSenderException;
import org.asl.middleware.database.dao.impl.ClientDAO;
import org.asl.middleware.database.model.Message;

@SuppressWarnings("serial")
public class ReadMessageFromSenderRequest extends Request {
	
	private int sender;
	private int receiver;
	private Message message;
	
	public ReadMessageFromSenderRequest(int sender, int receiver) {
		this.sender = sender;
		this.receiver = receiver;
		this.message = null;
		this.exception = new ReadMessageFromSenderException();
	}
	
	public int getSender() {
		return sender;
	}

	public void setSender(int sender) {
		this.sender = sender;
	}

	public int getReceiver() {
		return receiver;
	}

	public void setReceiver(int receiver) {
		this.receiver = receiver;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
	
	@Override
	public void processOnMiddleware() {
		try {
			setMessage(ClientDAO.getClientDAO().readMessageFromSender(sender, receiver));
		} catch (ReadMessageFromSenderException e) {
			setException(e);
		}
	}
	
	@Override
	public void processOnClient() throws ASLException {
		if (!getException().carriesError()) {
			System.out.println("System successfully received " + (message != null ? 1 : 0) + " messages");
		} else {
			throw getException();
		}
		
	}
	
}
