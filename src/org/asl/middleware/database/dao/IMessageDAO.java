package org.asl.middleware.database.dao;

import org.asl.common.request.types.exceptions.SendMessageException;
import org.asl.common.timing.ASLTimer;

public interface IMessageDAO {
	
	public void sendMessage(int sender, int receiver, int queue, String content, ASLTimer timer, int requestId) throws SendMessageException;
}
