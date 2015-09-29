package org.asl.common.message.types;

import org.asl.common.message.Message;
import org.asl.middleware.database.SqlHandler;

/**
 * This class implements the messages which are sent between the clients
 * and the middleware instances.
 * @author Sandro
 */
@SuppressWarnings("serial")
public class ContentMessage extends Message {
	
	/**
	 * Construct a ContentMessage based on field values.
	 */
	public ContentMessage(MessageType type, int local_id, int sender, int receiver, int queue,
			long startTime, String content, String action) {
		super(type, local_id, sender, receiver, queue, startTime, content, action);
	}

	@Override
	public void processOnMiddleware() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processOnClient() {
		// TODO Auto-generated method stub
		
	}
}