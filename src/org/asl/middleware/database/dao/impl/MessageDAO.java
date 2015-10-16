package org.asl.middleware.database.dao.impl;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import org.asl.common.request.types.exceptions.GetNumberOfMessagesException;
import org.asl.common.request.types.exceptions.SendMessageException;
import org.asl.middleware.MiddlewareInfo;
import org.asl.middleware.database.config.ASLDatabase;
import org.asl.middleware.database.connectionpool.ConnectionWrapper;
import org.asl.middleware.database.dao.IMessageDAO;
import org.asl.middleware.database.dao.common.CommonDAO;
import org.asl.middleware.database.model.MessageTable;

public class MessageDAO implements IMessageDAO {

	public static MessageDAO getMessageDAO() {
		return new MessageDAO();
	}
	
	@Override
	public void sendMessage(int sender, int receiver, int queue, String content, int requestId, MiddlewareInfo mi) throws SendMessageException {
		try (ConnectionWrapper conn = ASLDatabase.getNewConnection().get()) {
			PreparedStatement sendMessage = conn.get().prepareStatement(MessageTable.SEND_MESSAGE_STRING);
			sendMessage.setInt(1, sender);
			sendMessage.setInt(2, receiver);
			sendMessage.setInt(3, queue);
			sendMessage.setString(4, content);
			ResultSet rs = CommonDAO.executeQuery(conn.get(), sendMessage, sender, requestId, mi);
		} catch (SQLException | IOException | InterruptedException | ExecutionException e) {
			throw new SendMessageException(e);
		}
	}
	
	@Override
	public int getNumberOfMessages(int clientId, int requestId, MiddlewareInfo mi) throws GetNumberOfMessagesException {
		try (ConnectionWrapper conn = ASLDatabase.getNewConnection().get()) {
			PreparedStatement getNumberOfMessages = conn.get().prepareStatement(MessageTable.GET_NUMBER_OF_MESSAGES_STRING);
			ResultSet rs = CommonDAO.executeQuery(conn.get(), getNumberOfMessages, clientId, requestId, mi);
			rs.next();
			return rs.getInt(1);
		} catch (SQLException | IOException | InterruptedException | ExecutionException e) {
			throw new GetNumberOfMessagesException(e);
		}
	}
	
}
