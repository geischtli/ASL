package org.asl.common.message.types;

import org.asl.common.message.Message;
import org.asl.middleware.database.SqlHandler;

/**
 * StatusMessage represents the objects which are used as answers
 * for clients w.r.t their sent messages. These StatusMessages are
 * sent from the middleware which gets direct feedback from the
 * database and can evaluate the success of a querry and forward
 * with such an object to the client.
 * @author Sandro
 */
@SuppressWarnings("serial")
public class StatusMessage extends Message {
	
	/**
	 * Construct a StatusMessage based on field values.
	 */
	public StatusMessage(MessageType type, int local_id, int sender, int receiver, int queue,
			long startTime, String content, String action) {
		super(type,local_id, sender, receiver, queue, startTime, content, action);
	}

	@Override
	public void processOnMiddleware(SqlHandler sqlHandler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processOnClient() {
		// TODO Auto-generated method stub
		
	}
}