package org.asl.common.request.types.client;

import org.asl.common.request.Request;

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
		
	}

	@Override
	public void processOnClient() {
		
	}

	
	
	
}
