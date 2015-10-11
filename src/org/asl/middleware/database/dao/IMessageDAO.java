package org.asl.middleware.database.dao;

import org.asl.common.request.types.exceptions.SendMessageException;
import org.asl.common.timing.TimeLogger;

public interface IMessageDAO {
	
	public void sendMessage(int sender, int receiver, int queue, String content, TimeLogger timer, int requestId) throws SendMessageException;
}
