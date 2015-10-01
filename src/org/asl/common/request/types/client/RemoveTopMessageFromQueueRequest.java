package org.asl.common.request.types.client;

import org.asl.common.request.Request;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.request.types.exceptions.RemoveTopMessageFromQueueException;
import org.asl.middleware.database.dao.impl.QueueDAO;
import org.asl.middleware.database.model.Message;

@SuppressWarnings("serial")
public class RemoveTopMessageFromQueueRequest extends Request {

	private int receiver;
	private int queue;
	private Message message; 
	
	public RemoveTopMessageFromQueueRequest(int receiver, int queue) {
		this.receiver = receiver;
		this.queue = queue;
		this.message = null;
		this.exception = new RemoveTopMessageFromQueueException();
	}
	
	public int getReceiver() {
		return receiver;
	}
	
	public void setReceiver(int receiver) {
		this.receiver = receiver;
	}

	public int getQueue() {
		return queue;
	}

	public void setQueue(int queue) {
		this.queue = queue;
	}
	
	public Message getMessage() {
		return message;
	}

	public void setMessage(Message msg) {
		this.message = msg;
	}
	
	@Override
	public void processOnMiddleware() {
		try {
			setMessage(QueueDAO.getQueueDAO().removeTopMessageFromQueue(receiver, queue));
		} catch (RemoveTopMessageFromQueueException e) {
			setException(e);
		}
	}

	@Override
	public void processOnClient() throws ASLException {
		if (!getException().carriesError()) {
			System.out.println("Client received message with content: " + message.getContent());
		} else {
			throw getException();
		}
	}

}
