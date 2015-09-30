package org.asl.common.request.types.client;

import org.asl.common.request.Request;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.request.types.exceptions.RemoveTopMessageFromQueueException;
import org.asl.common.request.types.exceptions.SendMessageException;
import org.asl.middleware.database.dao.impl.MessageDAO;
import org.asl.middleware.database.dao.impl.QueueDAO;
import org.asl.middleware.database.model.Message;

@SuppressWarnings("serial")
public class RemoveTopMessageFromQueueRequest extends Request {

	private int queue;
	private int receiver;
	private Message msg; 
	
	public RemoveTopMessageFromQueueRequest(int queue, int receiver) {
		this.queue = queue;
		this.receiver = receiver;
		this.msg = null;
	}
	
	public int getQueue() {
		return queue;
	}

	public void setQueue(int queue) {
		this.queue = queue;
	}
	
	public int getReceiver() {
		return receiver;
	}
	
	public void setReceiver(int receiver) {
		this.receiver = receiver;
	}

	public Message getMsg() {
		return msg;
	}

	public void setMsg(Message msg) {
		this.msg = msg;
	}
	
	@Override
	public void processOnMiddleware() {
		try {
			setMsg(QueueDAO.getQueueDAO().removeTopMessageFromQueue(receiver, queue));
		} catch (RemoveTopMessageFromQueueException e) {
			setException(e);
		}
	}

	@Override
	public void processOnClient() throws ASLException {
		
	}

}
