package org.asl.middleware.database.dao;

import java.util.List;

import org.asl.common.request.types.exceptions.CreateQueueException;
import org.asl.common.request.types.exceptions.DeleteQueueException;
import org.asl.common.request.types.exceptions.GetQueuesWithMessagesForClientException;
import org.asl.common.request.types.exceptions.ReadAllMessagesOfQueueException;
import org.asl.common.request.types.exceptions.RemoveTopMessageFromQueueException;
import org.asl.middleware.database.model.Message;

public interface IQueueDAO {

	public int createQueue(int creator_id) throws CreateQueueException;
	public int deleteQueue(int queue_id) throws DeleteQueueException;
	public Message removeTopMessageFromQueue(int receiver, int queue) throws RemoveTopMessageFromQueueException;
	public List<Message> readAllMessagesOfQueue(int receiver, int queue) throws ReadAllMessagesOfQueueException;
	public List<Integer> getQueuesWithMessagesForClient(int receiver) throws GetQueuesWithMessagesForClientException;
}
