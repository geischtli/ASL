package org.asl.common.request.types.client;

import org.asl.client.ClientInfo;
import org.asl.common.request.Request;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.request.types.exceptions.DeleteQueueException;
import org.asl.common.timer.middleware.MiddlewareTimer;
import org.asl.middleware.database.dao.impl.QueueDAO;

public class DeleteQueueRequest extends Request {

	private static final long serialVersionUID = 103L;
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
	public void processOnMiddleware(MiddlewareTimer timer, int reqCount) {
		try {
			QueueDAO.getQueueDAO().deleteQueue(queue_id, timer, reqCount);
		} catch (DeleteQueueException e) {
			setException(e);
		}
	}

	@Override
	public void processOnClient(ClientInfo ci) throws ASLException {
		if (!getException().carriesError()) {
			System.out.println("Queue " + queue_id + " delete successfully");
			ci.setDeleteQueueId(0);
			ci.setReadQueueId(0);
			ci.setSendQueueId(0);
		} else {
			throw getException();
		}
	}
	
}
