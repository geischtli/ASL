package org.asl.middleware.database.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.asl.common.message.types.exceptions.CreateQueueException;
import org.asl.middleware.database.config.ASLDatabase;
import org.asl.middleware.database.dao.IQueueDAO;
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
}
