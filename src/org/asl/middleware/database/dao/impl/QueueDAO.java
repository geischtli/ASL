//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

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
import org.asl.common.request.types.exceptions.GetRegisteredQueuesException;
import org.asl.common.request.types.exceptions.ReadAllMessagesOfQueueException;
import org.asl.common.request.types.exceptions.RemoveTopMessageFromQueueException;
import org.asl.middleware.MiddlewareInfo;
import org.asl.middleware.database.config.ASLDatabase;
import org.asl.middleware.database.connectionpool.ConnectionWrapper;
import org.asl.middleware.database.dao.IQueueDAO;
import org.asl.middleware.database.dao.common.CommonDAO;
import org.asl.middleware.database.model.Message;
import org.asl.middleware.database.model.QueueTable;

public class QueueDAO implements IQueueDAO {

	public static QueueDAO getQueueDAO() {
		return new QueueDAO();
	}
	
	@Override
	public int createQueue(int creatorId, int requestId, MiddlewareInfo mi) throws CreateQueueException {
		try (ConnectionWrapper conn = ASLDatabase.getNewConnection().get()) {
			PreparedStatement createQueue = conn.get().prepareStatement(QueueTable.CREATE_QUEUE_STRING);
			createQueue.setInt(1, creatorId);
			ResultSet rs = CommonDAO.executeQuery(conn.get(), createQueue, creatorId, requestId, mi);
			rs.next();
			return rs.getInt(1);
		} catch (SQLException | IOException | InterruptedException | ExecutionException e) {
			throw new CreateQueueException(e);
		}
	}
	
	@Override
	public int deleteQueue(int queueId, int clientId, int requestId, MiddlewareInfo mi) throws DeleteQueueException {
		try (ConnectionWrapper conn = ASLDatabase.getNewConnection().get()) {
			PreparedStatement deleteQueue = conn.get().prepareStatement(QueueTable.DELETE_QUEUE_STRING);
			deleteQueue.setInt(1, queueId);
			ResultSet rs = CommonDAO.executeQuery(conn.get(), deleteQueue, clientId, requestId, mi);
			rs.next();
			return rs.getInt(1);
		} catch (SQLException | IOException | InterruptedException | ExecutionException e) {
			throw new DeleteQueueException(e);
		}
	}
	
	@Override
	public Message removeTopMessageFromQueue(int receiver, int queue, int clientId, int requestId, MiddlewareInfo mi) throws RemoveTopMessageFromQueueException {
		try (ConnectionWrapper conn = ASLDatabase.getNewConnection().get()) {
			PreparedStatement getMessage = conn.get().prepareStatement(QueueTable.REMOVE_TOP_MESSAGE_STRING);
			getMessage.setInt(1, receiver);
			getMessage.setInt(2, queue);
			ResultSet rs = CommonDAO.executeQuery(conn.get(), getMessage, clientId, requestId, mi);
			rs.next();
			return new Message(
					rs.getInt(1),
					rs.getInt(2),
					rs.getInt(3),
					rs.getInt(4),
					rs.getString(5),
					rs.getTimestamp(6)
			);
		} catch (SQLException | IOException | InterruptedException | ExecutionException e) {
			throw new RemoveTopMessageFromQueueException(e);
		}
	}

	@Override
	public List<Message> readAllMessagesOfQueue(int receiver, int queue,  int clientId, int requestId, MiddlewareInfo mi) throws ReadAllMessagesOfQueueException {
		try (ConnectionWrapper conn = ASLDatabase.getNewConnection().get()) {
			PreparedStatement getMessages = conn.get().prepareStatement(QueueTable.READ_ALL_MESSAGES_STRING);
			getMessages.setInt(1, receiver);
			getMessages.setInt(2, queue);
			ResultSet rs = CommonDAO.executeQuery(conn.get(), getMessages, clientId, requestId, mi);
			List<Message> messages = new ArrayList<Message>();
			while(rs.next()) {
				messages.add(new Message(
						rs.getInt(1),
						rs.getInt(2),
						rs.getInt(3),
						rs.getInt(4),
						rs.getString(5),
						rs.getTimestamp(6)
						)
				);
			}
			return messages;
		} catch (SQLException | IOException | InterruptedException | ExecutionException e) {
			throw new ReadAllMessagesOfQueueException(e);
		}
	}

	@Override
	public List<Integer> getQueuesWithMessagesForClient(int receiver, int clientId, int requestId, MiddlewareInfo mi) throws GetQueuesWithMessagesForClientException {
		try (ConnectionWrapper conn = ASLDatabase.getNewConnection().get()) {
			PreparedStatement getQueuesForClient = conn.get().prepareStatement(QueueTable.GET_QUEUES_FOR_CLIENT_STRING);
			getQueuesForClient.setInt(1, receiver);
			ResultSet rs = CommonDAO.executeQuery(conn.get(), getQueuesForClient, clientId, requestId, mi);
			List<Integer> queues = new ArrayList<Integer>();
			while (rs.next()) {
				queues.add(rs.getInt(1));
			}
			return queues;
		} catch (SQLException | IOException | InterruptedException | ExecutionException e) {
			throw new GetQueuesWithMessagesForClientException(e);
		}
	}

	@Override
	public List<Integer> getRegisteredQueues(int clientId, int requestId, MiddlewareInfo mi) throws GetRegisteredQueuesException {
		try (ConnectionWrapper conn = ASLDatabase.getNewConnection().get()) {
			PreparedStatement getRegisteredQueues = conn.get().prepareStatement(QueueTable.GET_REGISTERED_QUEUES_STRING);
			ResultSet rs = CommonDAO.executeQuery(conn.get(), getRegisteredQueues, clientId, requestId, mi);
			List<Integer> queues = new ArrayList<Integer>();
			while (rs.next()) {
				queues.add(rs.getInt(1));
			}
			return queues;
		} catch (SQLException | IOException | InterruptedException | ExecutionException e) {
			throw new GetRegisteredQueuesException(e);
		}
	}
	
}
