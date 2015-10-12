package org.asl.middleware.database.dao;

import org.asl.common.request.types.exceptions.GetNumberOfMessagesException;
import org.asl.common.request.types.exceptions.SendMessageException;

public interface IMessageDAO {
	
	public void sendMessage(int sender, int receiver, int queue, String content, int requestId) throws SendMessageException;
	public int getNumberOfMessages(int clientId, int requestId) throws GetNumberOfMessagesException;
}
