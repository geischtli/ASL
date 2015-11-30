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
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.asl.common.dateTuple.DateTriple;
import org.asl.common.request.types.exceptions.GetNumberOfMessagesException;
import org.asl.common.request.types.exceptions.SendMessageException;
import org.asl.middleware.Middleware;
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
	public DateTriple sendMessage(int sender, int receiver, int queue, String content, int requestId, MiddlewareInfo mi) throws SendMessageException {
		int currQueueLength = Middleware.currDbConnQueueLength.getAndIncrement();
		Middleware.dbConnQueueLengthPerSec.addAndGet(currQueueLength);
		long startWait = System.nanoTime();
		try (ConnectionWrapper conn = ASLDatabase.getNewConnection().get()) {
			Middleware.waitForDbConnPerSec.addAndGet(System.nanoTime() - startWait);
			Middleware.currDbConnQueueLength.decrementAndGet();
			PreparedStatement sendMessage = conn.get().prepareStatement(MessageTable.SEND_MESSAGE_STRING);
			sendMessage.setInt(1, sender);
			sendMessage.setInt(2, receiver);
			sendMessage.setInt(3, queue);
			sendMessage.setString(4, content);
			Date sendTime = new Date();
			ResultSet rs = CommonDAO.executeQuery(conn.get(), sendMessage, sender, requestId, mi);
			Date returnTime = new Date();
			rs.next();
			return new DateTriple(sendTime, rs.getTimestamp(1), returnTime);
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
