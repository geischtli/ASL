package org.asl.common.request.types.client;

import org.asl.client.ClientInfo;
import org.asl.common.request.Request;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.request.types.exceptions.DeleteQueueException;
import org.asl.middleware.MiddlewareInfo;
import org.asl.middleware.database.dao.impl.QueueDAO;

public class DeleteQueueRequest extends Request {

	private static final long serialVersionUID = 103L;
	private int queueId;
	
	public DeleteQueueRequest(int queueId, int clientId, int requestId) {
		super(clientId, requestId);
		this.queueId = queueId;
	}
	
	public int getQueueId() {
		return queueId;
	}

	public void setQueueId(int queue_id) {
		this.queueId = queue_id;
	}
	
	@Override
	public void processOnMiddleware(MiddlewareInfo mi) {
		try {
			QueueDAO.getQueueDAO().deleteQueue(queueId, clientId, requestId, mi);
		} catch (DeleteQueueException e) {
			setException(e);
		}
	}

	@Override
	public void processOnClient(ClientInfo ci) throws ASLException {
		if (!getException().carriesError()) {
			System.out.println("Queue " + queueId + " delete successfully");
			ci.setDeleteQueueId(0);
			ci.setReadQueueId(0);
			ci.setSendQueueId(0);
		} else {
			throw getException();
		}
	}
	
}
