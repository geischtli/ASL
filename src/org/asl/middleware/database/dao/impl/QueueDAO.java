package org.asl.middleware.database.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.asl.common.request.types.exceptions.CreateQueueException;
import org.asl.common.request.types.exceptions.GetQueuesWithMessagesForClientException;
import org.asl.common.request.types.exceptions.ReadAllMessagesOfQueueException;
import org.asl.common.request.types.exceptions.RemoveTopMessageFromQueueException;
import org.asl.middleware.database.config.ASLDatabase;
import org.asl.middleware.database.dao.IQueueDAO;
import org.asl.middleware.database.model.Message;
import org.asl.middleware.database.model.QueueTable;

public class QueueDAO implements IQueueDAO {

	public static QueueDAO getQueueDAO() {
		return new QueueDAO();
	}
	
	@Override
	public int createQueue(int creator_id) throws CreateQueueException {
		try (Connection conn = ASLDatabase.getNewConnection()) {
			PreparedStatement create_queue = conn.prepareStatement(QueueTable.CREATE_QUEUE_STRING);
			create_queue.setInt(1, creator_id);
			ResultSet rs = create_queue.executeQuery();
			conn.commit();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			throw new CreateQueueException(e);
		}
	}
	
	@Override
	public Message removeTopMessageFromQueue(int receiver, int queue) throws RemoveTopMessageFromQueueException {
		try (Connection conn = ASLDatabase.getNewConnection()) {
			PreparedStatement getMessage = conn.prepareStatement(QueueTable.REMOVE_TOP_MESSAGE_STRING);
			getMessage.setInt(1, receiver);
			getMessage.setInt(2, queue);
			ResultSet rs = getMessage.executeQuery();
			conn.commit();
			rs.next();
			return new Message(
					rs.getInt(1),
					rs.getInt(2),
					rs.getInt(3),
					rs.getInt(4),
					rs.getString(5)
			);
		} catch (SQLException e) {
			throw new RemoveTopMessageFromQueueException(e);
		}
	}

	@Override
	public List<Message> readAllMessagesOfQueue(int receiver, int queue) throws ReadAllMessagesOfQueueException {
		try (Connection conn = ASLDatabase.getNewConnection()) {
			PreparedStatement getMessages = conn.prepareStatement(QueueTable.READ_ALL_MESSAGES_STRING);
			getMessages.setInt(1, receiver);
			getMessages.setInt(2, queue);
			ResultSet rs = getMessages.executeQuery();
			conn.commit();
			List<Message> messages = new ArrayList<Message>();
			while(rs.next()) {
				messages.add(new Message(
						rs.getInt(1),
						rs.getInt(2),
						rs.getInt(3),
						rs.getInt(4),
						rs.getString(5)
						)
				);
			}
			return messages;
		} catch (SQLException e) {
			throw new ReadAllMessagesOfQueueException(e);
		}
	}

	@Override
	public List<Integer> getQueuesWithMessagesForClient(int receiver) throws GetQueuesWithMessagesForClientException {
		try (Connection conn = ASLDatabase.getNewConnection()) {
			PreparedStatement getQueuesForClient = conn.prepareStatement(QueueTable.GET_QUEUES_FOR_CLIENT_STRING);
			getQueuesForClient.setInt(1, receiver);
			ResultSet rs = getQueuesForClient.executeQuery();
			conn.commit();
			List<Integer> queues = new ArrayList<Integer>();
			while (rs.next()) {
				queues.add(rs.getInt(1));
			}
			return queues;
		} catch (SQLException e) {
			throw new GetQueuesWithMessagesForClientException(e);
		}
	}
	
}
