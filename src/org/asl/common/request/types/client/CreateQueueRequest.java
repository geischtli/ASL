package org.asl.common.request.types.client;

import org.asl.client.ClientInfo;
import org.asl.common.request.Request;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.request.types.exceptions.CreateQueueException;
import org.asl.middleware.database.dao.impl.QueueDAO;

@SuppressWarnings("serial")
public class CreateQueueRequest extends Request {

	private int queue_id;
	private int creator_id;
	
	public CreateQueueRequest(int creator_id) {
		this.creator_id = creator_id;
		this.exception = new CreateQueueException();
	}

	public int getQueueId() {
		return queue_id;
	}

	public void setQueueId(int queue_id) {
		this.queue_id = queue_id;
	}
	
	public int getCreatorId() {
		return creator_id;
	}

	public void setCreatorId(int creator_id) {
		this.creator_id = creator_id;
	}
	
	@Override
	public void processOnMiddleware() {
		try {
			setQueueId(QueueDAO.getQueueDAO().createQueue(creator_id));
		} catch (CreateQueueException e) {
			setException(e);
		}
	}

	@Override
	public void processOnClient() throws ASLException {
		if (!getException().carriesError()) {
			ClientInfo.setQueueId(queue_id);
			System.out.println("Queue created with id: " + ClientInfo.getQueueId());
		} else {
			throw getException();
		}
	}
}
