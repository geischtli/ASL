package org.asl.common.request.types.client;

import java.util.List;

import org.asl.common.request.Request;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.request.types.exceptions.GetQueuesWithMessagesForClientException;
import org.asl.middleware.database.dao.impl.QueueDAO;

@SuppressWarnings("serial")
public class GetQueuesWithMessagesForClientRequest extends Request {

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
	public void processOnMiddleware() {
		try {
			setQueues(QueueDAO.getQueueDAO().getQueuesWithMessagesForClient(receiver));
		} catch (GetQueuesWithMessagesForClientException e) {
			setException(e);
		}
	}

	@Override
	public void processOnClient() throws ASLException {
		if (!getException().carriesError()) {
			System.out.println("Successfully got queues " + queues.size() + " with waiting messages for client");
			if (queues.size() > 0) {
				System.out.print("Queues are: " );
				for (int i = 0; i < queues.size(); i++) {
					System.out.print(queues.get(i) + " ");
				}
			}
		} else {
			throw getException();
		}
	}

}
