package org.asl.middleware.database.dao;

import java.util.List;

import org.asl.common.request.Request;
import org.asl.middleware.clientsession.ClientSession;

public interface IMessageDAO {
	
	/**
	 * Create a queue, get next logical index.
	 */
	void createQueue();
	
	/**
	 * Create queue with specified index.
	 */
	void createQueue(int queue);
	
	/**
	 * Delete a queue.
	 */
	void deleteQueue(int queue);
	
	/**
	 * Get All Message By Queue
	 */
	List<Request> getMessagesOfQueue(int queue);
	
	/**
	 * Get Messages by Queue and Receiver (Actual Client)
	 * 
	 * @return Da Messages
	 */
	List<Request> getMessagesOfQueueAndReceiver(int queue, ClientSession session);
	
	/**
	 * Pop topmost message of this queue.
	 */
	Request popMessageOfQueue(int queue);
	
	/**
	 * Pop topmost message of this queue by receiver.
	 */
	Request popMessageOfQueueAndReceiver(int queue, ClientSession session);
	
	/**
	 * Send message to queue without specifying receiver.
	 */
	void sendMessageToQueue(Request msg, int queue);
	
	/**
	 * Send message to queue with specified receiver.
	 */
	void sendMessageToQueueWithReceiver(Request msg, int queue, ClientSession session);
	
	/**
	 * Get at most one message from a particular sender.
	 */
	Request getMessagesOfParticularSender(ClientSession session);
	
	/**
	 * Query for queues where messages are waiting for this client. 
	 */
	List<Integer> getQueuesWhereMessagesForThisClientReady(ClientSession session);
	
}
