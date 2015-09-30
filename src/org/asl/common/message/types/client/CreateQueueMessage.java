package org.asl.common.message.types.client;

import org.asl.client.ClientInfo;
import org.asl.common.message.Message;
import org.asl.common.message.types.exceptions.ASLException;
import org.asl.common.message.types.exceptions.CreateQueueException;
import org.asl.middleware.database.dao.impl.QueueDAO;

@SuppressWarnings("serial")
public class CreateQueueMessage extends Message {

	private int queue_id;
	
	
	public CreateQueueMessage(int queue_id) {
		this.setQueueId(queue_id);
		this.exception = new CreateQueueException();
	}

	public int getQueueId() {
		return queue_id;
	}

	public void setQueueId(int queue_id) {
		this.queue_id = queue_id;
	}
	
	@Override
	public void processOnMiddleware() {
		try {
			setQueueId(QueueDAO.getQueueDAO().createQueue());
			setException(new CreateQueueException(new Exception()));
		} catch (CreateQueueException e) {
			setException(e);
		}
	}

	@Override
	public void processOnClient() throws ASLException {
		if (!getException().carriesError()) {
			ClientInfo.setQueueId(queue_id);
		} else {
			throw getException();
		}
	}
}
