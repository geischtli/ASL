package org.asl.common.request.types;

import org.asl.common.request.Request;

/**
 * SqlMessage represents the object which embedds an sql
 * querry to be executed on the database and sent by a
 * client (e.g. create a new queue).
 * @author Sandro
 */
@SuppressWarnings("serial")
public class SqlMessage extends Request {
	
	/**
	 * Construct a SqlMessage based on field values.
	 */
	public SqlMessage(MessageType type, int local_id, int sender, int receiver, int queue,
			long startTime, String content, String action) {
		super(type, local_id, sender, receiver, queue, startTime, content, action);
	}

	@Override
	public void processOnMiddleware() {
		System.out.println("Hello from Thread " + Thread.currentThread().getId());
	}

	@Override
	public void processOnClient() {
		
	}
}