package org.asl.middleware.database.dao.impl;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import org.asl.common.request.types.exceptions.SendMessageException;
import org.asl.common.timing.middleware.MiddlewareTimer;
import org.asl.common.timing.middleware.MiddlewareTimings;
import org.asl.middleware.database.config.ASLDatabase;
import org.asl.middleware.database.connectionpool.ConnectionWrapper;
import org.asl.middleware.database.dao.IMessageDAO;
import org.asl.middleware.database.model.MessageTable;

public class MessageDAO implements IMessageDAO {

	public static MessageDAO getMessageDAO() {
		return new MessageDAO();
	}
	
	@Override
	public void sendMessage(int sender, int receiver, int queue, String content, MiddlewareTimer timer, int requestId) throws SendMessageException {
		try (ConnectionWrapper conn = ASLDatabase.getNewConnection().get()) {
			PreparedStatement sendMessage = conn.get().prepareStatement(MessageTable.SEND_MESSAGE_STRING);
			sendMessage.setInt(1, sender);
			sendMessage.setInt(2, receiver);
			sendMessage.setInt(3, queue);
			sendMessage.setString(4, content);
			timer.click(MiddlewareTimings.GOT_CONNECTION, requestId);
			sendMessage.execute();
			conn.get().commit();
			timer.click(MiddlewareTimings.EXECUTED_QUERY, requestId);
		} catch (SQLException | IOException | InterruptedException | ExecutionException e) {
			throw new SendMessageException(e);
		}
	}
	
}
