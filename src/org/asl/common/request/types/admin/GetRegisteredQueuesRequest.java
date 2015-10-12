package org.asl.common.request.types.admin;

import java.util.ArrayList;
import java.util.List;

import org.asl.client.ClientInfo;
import org.asl.common.request.Request;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.request.types.exceptions.GetRegisteredQueuesException;
import org.asl.common.timing.TimeLogger;
import org.asl.middleware.database.dao.impl.QueueDAO;

public class GetRegisteredQueuesRequest extends Request {

	private static final long serialVersionUID = 113L;
	private List<Integer> queues;
	
	public GetRegisteredQueuesRequest() {
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
	public void processOnMiddleware(TimeLogger timer, int reqCount) {
		try {
			System.out.println("in proc on mw");
			setQueues(QueueDAO.getQueueDAO().getRegisteredQueues());
		} catch (GetRegisteredQueuesException e) {
			setException(e);
		}
	}

	@Override
	public void processOnClient(ClientInfo ci) throws ASLException {
		System.out.println("in proc on client");
		if (!getException().carriesError()) {
			if (queues.size() > 0) {
				ci.setQueuesOnline(queues);
			} else {
				ci.setQueuesOnline(new ArrayList<Integer>());
			}
		} else {
			throw getException();
		}
	}

	

}
