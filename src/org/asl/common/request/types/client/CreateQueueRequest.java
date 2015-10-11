package org.asl.common.request.types.client;

import org.asl.client.ClientInfo;
import org.asl.common.request.Request;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.request.types.exceptions.CreateQueueException;
import org.asl.common.timing.TimeLogger;
import org.asl.middleware.database.dao.impl.QueueDAO;

public class CreateQueueRequest extends Request {

	private static final long serialVersionUID = 102L;
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
	public void processOnMiddleware(TimeLogger timer, int reqCount) {
		try {
			setQueueId(QueueDAO.getQueueDAO().createQueue(creator_id, timer, reqCount));
		} catch (CreateQueueException e) {
			setException(e);
		}
	}

	@Override
	public void processOnClient(ClientInfo ci) throws ASLException {
		if (!getException().carriesError()) {
			ci.setDeleteQueueId(queue_id);
			ci.setSendQueueId(queue_id);
			ci.setReadQueueId(queue_id);
			System.out.println("Queue created with id: " + ci.getDeleteQueueId());
		} else {
			throw getException();
		}
	}
}
