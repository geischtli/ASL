package org.asl.common.request.types.client;

import org.asl.client.ClientInfo;
import org.asl.common.request.Request;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.request.types.exceptions.SendMessageException;
import org.asl.middleware.database.dao.impl.MessageDAO;

@SuppressWarnings("serial")
public class SendMessageRequest extends Request {
	
	private final int sender;
	private final int receiver;
	private final int queue;
	private final String content;
	
	public SendMessageRequest(int sender, int receiver, int queue, String content) {
		this.sender = sender;
		this.receiver = receiver;
		this.queue = queue;
		this.content = content;
		this.exception = new SendMessageException();
	}
	
	public int getSender() {
		return sender;
	}

	public int getReceiver() {
		return receiver;
	}

	public int getQueue() {
		return queue;
	}
	
	public String getMessage() {
		return content;
	}
	
	@Override
	public void processOnMiddleware() {
		try {
			MessageDAO.getMessageDAO().sendMessage(sender, receiver, queue, content);
		} catch (SendMessageException e) {
			setException(e);
		}
	}

	@Override
	public void processOnClient() throws ASLException {
		if (!getException().carriesError()) {
			System.out.println("Message successfully sent");
		} else {
			throw getException();
		}
	}

}
