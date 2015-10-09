package org.asl.middleware.database.dao;

import java.util.List;

import org.asl.common.request.types.exceptions.CreateQueueException;
import org.asl.common.request.types.exceptions.DeleteQueueException;
import org.asl.common.request.types.exceptions.GetQueuesWithMessagesForClientException;
import org.asl.common.request.types.exceptions.ReadAllMessagesOfQueueException;
import org.asl.common.request.types.exceptions.RemoveTopMessageFromQueueException;
import org.asl.common.timing.ASLTimer;
import org.asl.middleware.database.model.Message;

public interface IQueueDAO {

	public int createQueue(int creator_id, ASLTimer timer, int reqCount) throws CreateQueueException;
	public int deleteQueue(int queue_id, ASLTimer timer, int requestId) throws DeleteQueueException;
	public Message removeTopMessageFromQueue(int receiver, int queue, ASLTimer timer, int requestId) throws RemoveTopMessageFromQueueException;
	public List<Message> readAllMessagesOfQueue(int receiver, int queue, ASLTimer timer, int requestId) throws ReadAllMessagesOfQueueException;
	public List<Integer> getQueuesWithMessagesForClient(int receiver, ASLTimer timer, int requestId) throws GetQueuesWithMessagesForClientException;
}
