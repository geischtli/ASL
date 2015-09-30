package org.asl.common.message.types.client;

import org.asl.common.message.Message;

@SuppressWarnings("serial")
public class DeleteQueueMessage extends Message {

	private int queue_id;
	
	public DeleteQueueMessage(int queue_id) {
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
