//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

package org.asl.common.request.types.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.asl.client.ClientInfo;
import org.asl.common.request.Request;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.request.types.exceptions.GetRegisteredQueuesException;
import org.asl.middleware.MiddlewareInfo;
import org.asl.middleware.database.dao.impl.QueueDAO;

public class GetRegisteredQueuesRequest extends Request {

	private static final long serialVersionUID = 113L;
	private List<Integer> queues;
	
	public GetRegisteredQueuesRequest(int clientId, int requestId) {
		super(clientId, requestId);
		this.setQueues(null);
		this.exception = new GetRegisteredQueuesException();
	}
	
	public List<Integer> getQueues() {
		return queues;
	}

	public void setQueues(List<Integer> queues) {
		this.queues = queues;
	}
	
	@Override
	public void processOnMiddleware(MiddlewareInfo mi) {
		try {
			setQueues(QueueDAO.getQueueDAO().getRegisteredQueues(clientId, requestId, mi));
		} catch (GetRegisteredQueuesException e) {
			setException(e);
		}
	}

	@Override
	public void processOnClient(ClientInfo ci) throws ASLException {
		if (!getException().carriesError()) {
			if (queues.size() > 0) {
				ci.setQueuesOnline(queues);
				ci.setSendQueueId(queues.get(new Random().nextInt(queues.size())));
			} else {
				ci.setQueuesOnline(new ArrayList<Integer>());
			}
		} else {
			throw getException();
		}
	}

}
