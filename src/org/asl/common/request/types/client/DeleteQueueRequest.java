package org.asl.common.request.types.client;

import org.asl.common.request.Request;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.request.types.exceptions.DeleteQueueException;
import org.asl.middleware.database.dao.impl.QueueDAO;

@SuppressWarnings("serial")
public class DeleteQueueRequest extends Request {

	private int queue_id;
	
	public DeleteQueueRequest(int queue_id) {
		this.queue_id = queue_id;
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
			QueueDAO.getQueueDAO().deleteQueue(queue_id);
		} catch (DeleteQueueException e) {
			setException(e);
		}
	}

	@Override
	public void processOnClient() throws ASLException {
		if (!getException().carriesError()) {
			System.out.println("Queue " + queue_id + " delete successfully");
		} else {
			throw getException();
		}
	}
	
}
