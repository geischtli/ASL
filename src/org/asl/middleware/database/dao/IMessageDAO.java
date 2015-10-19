package org.asl.middleware.database.dao;

import org.asl.common.dateTuple.DateTriple;
import org.asl.common.request.types.exceptions.GetNumberOfMessagesException;
import org.asl.common.request.types.exceptions.SendMessageException;
import org.asl.middleware.MiddlewareInfo;

public interface IMessageDAO {
	
	public DateTriple sendMessage(int sender, int receiver, int queue, String content, int requestId, MiddlewareInfo mi) throws SendMessageException;
	public int getNumberOfMessages(int clientId, int requestId, MiddlewareInfo mi) throws GetNumberOfMessagesException;
}
