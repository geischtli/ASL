package org.asl.common.request.types.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.asl.client.ClientInfo;
import org.asl.common.request.Request;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.request.types.exceptions.GetQueuesWithMessagesForClientException;
import org.asl.common.timing.TimeLogger;
import org.asl.middleware.database.dao.impl.QueueDAO;

public class GetQueuesWithMessagesForClientRequest extends Request {

	private static final long serialVersionUID = 104L;
	private int receiver;
	private List<Integer> queues;
	
	public GetQueuesWithMessagesForClientRequest(int receiver) {
		this.receiver = receiver;
		this.queues = null;
		this.exception = new GetQueuesWithMessagesForClientException();
	}
	
	public int getReceiver() {
		return receiver;
	}

	public void setReceiver(int receiver) {
		this.receiver = receiver;
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
			setQueues(QueueDAO.getQueueDAO().getQueuesWithMessagesForClient(receiver, timer, reqCount));
		} catch (GetQueuesWithMessagesForClientException e) {
			setException(e);
		}
	}

	@Override
	public void processOnClient(ClientInfo ci) throws ASLException {
		if (!getException().carriesError()) {
			if (queues.size() > 0) {
				ci.setQueuesOnline(queues);
				// set a random queue of the ones we get to next reading target
				ci.setReadQueueId(queues.get(new Random().nextInt(queues.size())));
			} else if (queues.size() == 0) {
				ci.setQueuesOnline(new ArrayList<Integer>());
			}
		} else {
			throw getException();
		}
	}

}
