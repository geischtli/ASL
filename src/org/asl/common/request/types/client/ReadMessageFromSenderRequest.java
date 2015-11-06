//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

package org.asl.common.request.types.client;

import org.asl.client.ClientInfo;
import org.asl.common.request.Request;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.request.types.exceptions.ReadMessageFromSenderException;
import org.asl.middleware.MiddlewareInfo;
import org.asl.middleware.database.dao.impl.ClientDAO;
import org.asl.middleware.database.model.Message;

public class ReadMessageFromSenderRequest extends Request {
	
	private static final long serialVersionUID = 108L;
	private int sender;
	private int receiver;
	private Message message;
	
	public ReadMessageFromSenderRequest(int sender, int receiver, int requestId) {
		super(sender, requestId);
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
	public void processOnMiddleware(MiddlewareInfo mi) {
		try {
			setMessage(ClientDAO.getClientDAO().readMessageFromSender(sender, receiver, clientId, requestId, mi));
		} catch (ReadMessageFromSenderException e) {
			setException(e);
		}
	}
	
	@Override
	public void processOnClient(ClientInfo ci) throws ASLException {
		if (!getException().carriesError()) {
			//System.out.println("System successfully received " + (message != null ? 1 : 0) + " messages");
		} else {
			throw getException();
		}
		
	}
	
}
