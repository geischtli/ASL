package org.asl.middleware.database.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.asl.common.request.types.exceptions.CreateQueueException;
import org.asl.common.request.types.exceptions.RemoveTopMessageFromQueueException;
import org.asl.common.request.types.exceptions.SendMessageException;
import org.asl.middleware.database.config.ASLDatabase;
import org.asl.middleware.database.dao.IQueueDAO;
import org.asl.middleware.database.model.Message;
import org.asl.middleware.database.model.MessageTable;
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
			return new Message(
					rs.getInt("ID"),
					rs.getInt("SENDER"),
					rs.getInt("RECEIVER"),
					rs.getInt("QUEUE"),
					rs.getString("CONTENT")
			);
		} catch (SQLException e) {
			throw new RemoveTopMessageFromQueueException(e);
		}
	}
}
