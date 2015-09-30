package org.asl.middleware.database.dao;

import org.asl.common.request.types.exceptions.CreateQueueException;
import org.asl.common.request.types.exceptions.RemoveTopMessageFromQueueException;
import org.asl.common.request.types.exceptions.SendMessageException;
import org.asl.middleware.database.model.Message;

public interface IQueueDAO {

	public int createQueue(int creator_id) throws CreateQueueException;
	public Message removeTopMessageFromQueue(int receiver, int queue) throws RemoveTopMessageFromQueueException;
}
