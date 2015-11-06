//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

package org.asl.middleware.database.dao;

import java.util.List;

import org.asl.common.request.types.exceptions.CreateQueueException;
import org.asl.common.request.types.exceptions.DeleteQueueException;
import org.asl.common.request.types.exceptions.GetQueuesWithMessagesForClientException;
import org.asl.common.request.types.exceptions.GetRegisteredQueuesException;
import org.asl.common.request.types.exceptions.ReadAllMessagesOfQueueException;
import org.asl.common.request.types.exceptions.RemoveTopMessageFromQueueException;
import org.asl.middleware.MiddlewareInfo;
import org.asl.middleware.database.model.Message;

public interface IQueueDAO {

	public int createQueue(int creator_id, int reqCount, MiddlewareInfo mi) throws CreateQueueException;
	public int deleteQueue(int queueId, int clientId, int requestId, MiddlewareInfo mi) throws DeleteQueueException;
	public Message removeTopMessageFromQueue(int receiver, int queue, int clientId, int requestId, MiddlewareInfo mi) throws RemoveTopMessageFromQueueException;
	public List<Message> readAllMessagesOfQueue(int receiver, int queue,  int clientId, int requestId, MiddlewareInfo mi) throws ReadAllMessagesOfQueueException;
	public List<Integer> getQueuesWithMessagesForClient(int receiver, int clientId, int requestId, MiddlewareInfo mi) throws GetQueuesWithMessagesForClientException;
	public List<Integer> getRegisteredQueues(int clientId, int requestId, MiddlewareInfo mi) throws GetRegisteredQueuesException;
	
}
