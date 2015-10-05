package org.asl.common.request.types.client;

import org.asl.client.ClientInfo;
import org.asl.common.request.Request;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.request.types.exceptions.RemoveTopMessageFromQueueException;
import org.asl.middleware.database.dao.impl.QueueDAO;
import org.asl.middleware.database.model.Message;

@SuppressWarnings("serial")
public class RemoveTopMessageFromQueueRequest extends Request {

	private static final long serialVersionUID = 109L;
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
			// we don't know if this was the last message of the queue, we have to ask again for a list of queues
			ClientInfo.setReadQueueId(0);
		} else {
			throw getException();
		}
	}

}
