package org.asl.middleware.database.dao.impl;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.asl.common.request.types.exceptions.GetRegisteredClientsException;
import org.asl.common.request.types.exceptions.HandshakeException;
import org.asl.common.request.types.exceptions.ReadMessageFromSenderException;
import org.asl.common.timing.ASLTimer;
import org.asl.middleware.MiddlewareInfo;
import org.asl.middleware.database.config.ASLDatabase;
import org.asl.middleware.database.connectionpool.ConnectionWrapper;
import org.asl.middleware.database.dao.IClientDAO;
import org.asl.middleware.database.model.ClientTable;
import org.asl.middleware.database.model.Message;

public class ClientDAO implements IClientDAO {

	public static ClientDAO getClientDAO() {
		return new ClientDAO();
	}
	
	@Override
	public int registerClient(ASLTimer timer, int requestId) throws HandshakeException {
		try (ConnectionWrapper conn = ASLDatabase.getNewConnection().get()) {
			PreparedStatement registerClient = conn.get().prepareStatement(ClientTable.REGISTER_CLIENT_STRING);
			registerClient.setInt(1, MiddlewareInfo.getMiddlewareId());
//			timer.click(ASLTimer.GOT_CONNECTION, requestId);
			ResultSet rs = registerClient.executeQuery();
			conn.get().commit();
//			timer.click(ASLTimer.EXECUTED_QUERY, requestId);
			rs.next();
			return rs.getInt(1);
		} catch (SQLException | IOException | InterruptedException | ExecutionException e) {
			throw new HandshakeException(e);
		}
	}

	@Override
	public Message readMessageFromSender(int sender, int receiver, ASLTimer timer, int requestId) throws ReadMessageFromSenderException {
		try (ConnectionWrapper conn = ASLDatabase.getNewConnection().get()) {
			PreparedStatement readMessageFromSender = conn.get().prepareStatement(ClientTable.READ_MESSAGE_FROM_SENDER);
			readMessageFromSender.setInt(1, sender);
			readMessageFromSender.setInt(2, receiver);
//			timer.click(MiddlewareTimings.GOT_CONNECTION, requestId);
			ResultSet rs = readMessageFromSender.executeQuery();
			conn.get().commit();
//			timer.click(MiddlewareTimings.EXECUTED_QUERY, requestId);
			// returns either 0 or 1 message
			if (rs.next()) {
				return new Message(
						rs.getInt(1),
						rs.getInt(2),
						rs.getInt(3),
						rs.getInt(4),
						rs.getString(5)
				);
			} else {
				return null;
			}
		} catch (SQLException | IOException | InterruptedException | ExecutionException e) {
			throw new ReadMessageFromSenderException(e);
		}
	}
	
	@Override
	public List<Integer> getRegisteredClients(ASLTimer timer, int requestId) throws GetRegisteredClientsException {
		try (ConnectionWrapper conn = ASLDatabase.getNewConnection().get()) {
			PreparedStatement getRegisteredClients = conn.get().prepareStatement(ClientTable.GET_REGISTERED_CLIENTS_STRING);
//			timer.click(MiddlewareTimings.GOT_CONNECTION, requestId);
			ResultSet rs = getRegisteredClients.executeQuery();
			conn.get().commit();
//			timer.click(MiddlewareTimings.EXECUTED_QUERY, requestId);
			List<Integer> clients = new ArrayList<Integer>();
			while (rs.next()) {
				clients.add(rs.getInt(1));
			}
			return clients;
		} catch (SQLException | IOException | InterruptedException | ExecutionException e) {
			throw new GetRegisteredClientsException(e);
		}
	}

}
