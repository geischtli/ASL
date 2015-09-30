package org.asl.middleware.database.dao;

import org.asl.common.request.types.exceptions.SendMessageException;

public interface IMessageDAO {
	
	public void sendMessage(int sender, int receiver, int queue, String content) throws SendMessageException;
	
}
