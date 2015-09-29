package org.asl.middleware.database.dao;

import java.util.List;

import org.asl.common.message.Message;
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
	List<Message> getMessagesOfQueue(int queue);
	
	/**
	 * Get Messages by Queue and Receiver (Actual Client)
	 * 
	 * @return Da Messages
	 */
	List<Message> getMessagesOfQueueAndReceiver(int queue, ClientSession session);
	
	/**
	 * Pop topmost message of this queue.
	 */
	Message popMessageOfQueue(int queue);
	
	/**
	 * Pop topmost message of this queue by receiver.
	 */
	Message popMessageOfQueueAndReceiver(int queue, ClientSession session);
	
	/**
	 * Send message to queue without specifying receiver.
	 */
	void sendMessageToQueue(Message msg, int queue);
	
	/**
	 * Send message to queue with specified receiver.
	 */
	void sendMessageToQueueWithReceiver(Message msg, int queue, ClientSession session);
	
	/**
	 * Get at most one message from a particular sender.
	 */
	Message getMessagesOfParticularSender(ClientSession session);
	
	/**
	 * Query for queues where messages are waiting for this client. 
	 */
	List<Integer> getQueuesWhereMessagesForThisClientReady(ClientSession session);
	
}
