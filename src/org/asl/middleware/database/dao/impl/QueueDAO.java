package org.asl.middleware.database.dao.impl;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.asl.common.request.types.exceptions.CreateQueueException;
import org.asl.common.request.types.exceptions.DeleteQueueException;
import org.asl.common.request.types.exceptions.GetQueuesWithMessagesForClientException;
import org.asl.common.request.types.exceptions.ReadAllMessagesOfQueueException;
import org.asl.common.request.types.exceptions.RemoveTopMessageFromQueueException;
import org.asl.common.timing.ASLTimer;
import org.asl.middleware.database.config.ASLDatabase;
import org.asl.middleware.database.connectionpool.ConnectionWrapper;
import org.asl.middleware.database.dao.IQueueDAO;
import org.asl.middleware.database.model.Message;
import org.asl.middleware.database.model.QueueTable;

public class QueueDAO implements IQueueDAO {

	public static QueueDAO getQueueDAO() {
		return new QueueDAO();
	}
	
	@Override
	public int createQueue(int creator_id, ASLTimer timer, int requestId) throws CreateQueueException {
		try (ConnectionWrapper conn = ASLDatabase.getNewConnection().get()) {
			PreparedStatement create_queue = conn.get().prepareStatement(QueueTable.CREATE_QUEUE_STRING);
			create_queue.setInt(1, creator_id);
//			timer.click(ASLTimer.GOT_CONNECTION, requestId);
			ResultSet rs = create_queue.executeQuery();
			conn.get().commit();
//			timer.click(ASLTimer.EXECUTED_QUERY, requestId);
			rs.next();
			return rs.getInt(1);
		} catch (SQLException | IOException | InterruptedException | ExecutionException e) {
			throw new CreateQueueException(e);
		}
	}
	
	@Override
	public int deleteQueue(int queue_id, ASLTimer timer, int requestId) throws DeleteQueueException {
		try (ConnectionWrapper conn = ASLDatabase.getNewConnection().get()) {
			PreparedStatement deleteQueue = conn.get().prepareStatement(QueueTable.DELETE_QUEUE_STRING);
			deleteQueue.setInt(1, queue_id);
//			timer.click(MiddlewareTimings.GOT_CONNECTION, requestId);
			ResultSet rs = deleteQueue.executeQuery();
			conn.get().commit();
//			timer.click(MiddlewareTimings.EXECUTED_QUERY, requestId);
			rs.next();
			return rs.getInt(1);
		} catch (SQLException | IOException | InterruptedException | ExecutionException e) {
			throw new DeleteQueueException(e);
		}
	}
	
	@Override
	public Message removeTopMessageFromQueue(int receiver, int queue, ASLTimer timer, int requestId) throws RemoveTopMessageFromQueueException {
		try (ConnectionWrapper conn = ASLDatabase.getNewConnection().get()) {
			PreparedStatement getMessage = conn.get().prepareStatement(QueueTable.REMOVE_TOP_MESSAGE_STRING);
			getMessage.setInt(1, receiver);
			getMessage.setInt(2, queue);
//			timer.click(MiddlewareTimings.GOT_CONNECTION, requestId);
			ResultSet rs = getMessage.executeQuery();
			conn.get().commit();
//			timer.click(MiddlewareTimings.EXECUTED_QUERY, requestId);
			rs.next();
			return new Message(
					rs.getInt(1),
					rs.getInt(2),
					rs.getInt(3),
					rs.getInt(4),
					rs.getString(5)
			);
		} catch (SQLException | IOException | InterruptedException | ExecutionException e) {
			throw new RemoveTopMessageFromQueueException(e);
		}
	}

	@Override
	public List<Message> readAllMessagesOfQueue(int receiver, int queue, ASLTimer timer, int requestId) throws ReadAllMessagesOfQueueException {
		try (ConnectionWrapper conn = ASLDatabase.getNewConnection().get()) {
			PreparedStatement getMessages = conn.get().prepareStatement(QueueTable.READ_ALL_MESSAGES_STRING);
			getMessages.setInt(1, receiver);
			getMessages.setInt(2, queue);
//			timer.click(MiddlewareTimings.GOT_CONNECTION, requestId);
			ResultSet rs = getMessages.executeQuery();
			conn.get().commit();
//			timer.click(MiddlewareTimings.EXECUTED_QUERY, requestId);
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
		} catch (SQLException | IOException | InterruptedException | ExecutionException e) {
			throw new ReadAllMessagesOfQueueException(e);
		}
	}

	@Override
	public List<Integer> getQueuesWithMessagesForClient(int receiver, ASLTimer timer, int requestId) throws GetQueuesWithMessagesForClientException {
		try (ConnectionWrapper conn = ASLDatabase.getNewConnection().get()) {
			PreparedStatement getQueuesForClient = conn.get().prepareStatement(QueueTable.GET_QUEUES_FOR_CLIENT_STRING);
			getQueuesForClient.setInt(1, receiver);
//			timer.click(MiddlewareTimings.GOT_CONNECTION, requestId);
			ResultSet rs = getQueuesForClient.executeQuery();
			conn.get().commit();
//			timer.click(MiddlewareTimings.EXECUTED_QUERY, requestId);
			List<Integer> queues = new ArrayList<Integer>();
			while (rs.next()) {
				queues.add(rs.getInt(1));
			}
			return queues;
		} catch (SQLException | IOException | InterruptedException | ExecutionException e) {
			throw new GetQueuesWithMessagesForClientException(e);
		}
	}
	
}
