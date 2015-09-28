package org.asl.middleware.database.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.asl.common.message.Message;
import org.asl.middleware.clientsession.ClientSession;
import org.asl.middleware.database.config.ASLDatabase;
import org.asl.middleware.database.dao.IMessageDAO;

public class MessageDAO implements IMessageDAO {
	
	@Override
	public void createQueue() {
		try (Connection conn = ASLDatabase.getNewConnection()) {
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createQueue(int queue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteQueue(int queue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Message> getMessagesOfQueue(int queue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Message> getMessagesOfQueueAndReceiver(int queue, ClientSession session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message popMessageOfQueue(int queue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message popMessageOfQueueAndReceiver(int queue, ClientSession session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendMessageToQueue(Message msg, int queue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendMessageToQueueWithReceiver(Message msg, int queue, ClientSession session) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Message getMessagesOfParticularSender(ClientSession session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> getQueuesWhereMessagesForThisClientReady(ClientSession session) {
		// TODO Auto-generated method stub
		return null;
	}
}
