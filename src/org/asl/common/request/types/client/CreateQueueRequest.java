//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

package org.asl.common.request.types.client;

import org.asl.client.ClientInfo;
import org.asl.common.request.Request;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.request.types.exceptions.CreateQueueException;
import org.asl.middleware.MiddlewareInfo;
import org.asl.middleware.database.dao.impl.QueueDAO;

public class CreateQueueRequest extends Request {

	private static final long serialVersionUID = 102L;
	private int queueId;
	private int creatorId;
	
	public CreateQueueRequest(int creatorId, int requestId) {
		super(creatorId, requestId);
		this.creatorId = creatorId;
		this.exception = new CreateQueueException();
	}

	public int getQueueId() {
		return queueId;
	}

	public void setQueueId(int queue_id) {
		this.queueId = queue_id;
	}
	
	public int getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(int creator_id) {
		this.creatorId = creator_id;
	}
	
	@Override
	public void processOnMiddleware(MiddlewareInfo mi) {
		try {
			setQueueId(QueueDAO.getQueueDAO().createQueue(creatorId, requestId, mi));
		} catch (CreateQueueException e) {
			setException(e);
		}
	}

	@Override
	public void processOnClient(ClientInfo ci) throws ASLException {
		if (!getException().carriesError()) {
			ci.setDeleteQueueId(queueId);
			ci.setSendQueueId(queueId);
			ci.setReadQueueId(queueId);
			System.out.println("Queue created with id: " + ci.getDeleteQueueId());
		} else {
			throw getException();
		}
	}
}
