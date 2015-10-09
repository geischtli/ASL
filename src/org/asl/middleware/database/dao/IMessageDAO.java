package org.asl.middleware.database.dao;

import org.asl.common.request.types.exceptions.SendMessageException;
import org.asl.common.timing.middleware.MiddlewareTimer;

public interface IMessageDAO {
	
	public void sendMessage(int sender, int receiver, int queue, String content, MiddlewareTimer timer, int requestId) throws SendMessageException;
}
