package org.asl.common.request.types.client;

import java.util.List;

import org.asl.common.request.Request;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.request.types.exceptions.ReadAllMessagesOfQueueException;
import org.asl.middleware.database.dao.impl.QueueDAO;
import org.asl.middleware.database.model.Message;

public class ReadAllMessagesOfQueueRequest extends Request {

	private static final long serialVersionUID = 107L;
	private int queue;
	private int receiver;
	private List<Message> messages;
	
	public ReadAllMessagesOfQueueRequest(int queue, int receiver) {
		this.queue = queue;
		this.receiver = receiver;
		this.messages = null;
		this.exception = new ReadAllMessagesOfQueueException();
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

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	
	@Override
	public void processOnMiddleware() {
		try {
			setMessages(QueueDAO.getQueueDAO().readAllMessagesOfQueue(receiver, queue));
		} catch (ReadAllMessagesOfQueueException e) {
			setException(e);
		}
	}

	@Override
	public void processOnClient() throws ASLException {
		if (!getException().carriesError()) {
			System.out.println("Successfully read " + messages.size() + " messages");
			/*System.out.println("Messages:");
			for (int i = 0; i < messages.size(); i++) {
				Message m = messages.get(i);
				System.out.println(
					m.getId() + ", " +
					m.getSender() + ", " +
					m.getReceiver() + ", " +
					m.getQueue() + ", " +
					m.getContent()
				);
			}*/
		} else {
			throw getException();
		}
	}

}
