package org.asl.middleware.database.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.asl.common.request.types.exceptions.SendMessageException;
import org.asl.middleware.database.config.ASLDatabase;
import org.asl.middleware.database.dao.IMessageDAO;
import org.asl.middleware.database.model.MessageTable;

public class MessageDAO implements IMessageDAO {

	public static MessageDAO getMessageDAO() {
		return new MessageDAO();
	}
	
	@Override
	public void sendMessage(int sender, int receiver, int queue, String content) throws SendMessageException {
		try (Connection conn = ASLDatabase.getNewConnection()) {
			PreparedStatement sendMessage = conn.prepareStatement(MessageTable.SEND_MESSAGE_STRING);
			sendMessage.setInt(1, sender);
			sendMessage.setInt(2, receiver);
			sendMessage.setInt(3, queue);
			sendMessage.setString(4, content);
			sendMessage.execute();
			conn.commit();
		} catch (SQLException e) {
			throw new SendMessageException(e);
		}
	}
	
	
	
}
