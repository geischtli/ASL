package org.asl.common.request.types.admin;

import java.util.ArrayList;
import java.util.List;

import org.asl.client.ClientInfo;
import org.asl.common.request.Request;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.request.types.exceptions.GetRegisteredQueuesException;
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
	public void processOnMiddleware() {
		try {
			setQueues(QueueDAO.getQueueDAO().getRegisteredQueues(clientId, requestId));
		} catch (GetRegisteredQueuesException e) {
			setException(e);
		}
	}

	@Override
	public void processOnClient(ClientInfo ci) throws ASLException {
		if (!getException().carriesError()) {
			if (queues.size() > 0) {
				System.out.println("Queues are " + (queues == null ? "null" : "not null"));
				ci.setQueuesOnline(queues);
			} else {
				ci.setQueuesOnline(new ArrayList<Integer>());
			}
		} else {
			throw getException();
		}
	}

	

}
