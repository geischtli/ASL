package org.asl.middleware.database.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.asl.common.request.types.exceptions.GetRegisteredClientsException;
import org.asl.common.request.types.exceptions.HandshakeException;
import org.asl.common.request.types.exceptions.ReadMessageFromSenderException;
import org.asl.middleware.MiddlewareInfo;
import org.asl.middleware.database.config.ASLDatabase;
import org.asl.middleware.database.dao.IClientDAO;
import org.asl.middleware.database.model.ClientTable;
import org.asl.middleware.database.model.Message;

public class ClientDAO implements IClientDAO {

	public static ClientDAO getClientDAO() {
		return new ClientDAO();
	}
	
	@Override
	public int registerClient() throws HandshakeException {
		try (Connection conn = ASLDatabase.getNewConnection()) {
			PreparedStatement registerClient = conn.prepareStatement(ClientTable.REGISTER_CLIENT_STRING);
			registerClient.setInt(1, MiddlewareInfo.getMiddlewareId());
			ResultSet rs = registerClient.executeQuery();
			rs.next();
			conn.commit();
			return rs.getInt(1);
		} catch (SQLException e) {
			throw new HandshakeException(e);
		}
	}

	@Override
	public Message readMessageFromSender(int sender, int receiver) throws ReadMessageFromSenderException {
		try (Connection conn = ASLDatabase.getNewConnection()) {
			PreparedStatement readMessageFromSender = conn.prepareStatement(ClientTable.READ_MESSAGE_FROM_SENDER);
			readMessageFromSender.setInt(1, sender);
			readMessageFromSender.setInt(2, receiver);
			ResultSet rs = readMessageFromSender.executeQuery();
			conn.commit();
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
		} catch (SQLException e) {
			throw new ReadMessageFromSenderException(e);
		}
	}
	
	@Override
	public List<Integer> getRegisteredClients() throws GetRegisteredClientsException {
		try (Connection conn = ASLDatabase.getNewConnection()) {
			PreparedStatement getRegisteredClients = conn.prepareStatement(ClientTable.GET_REGISTERED_CLIENTS_STRING);
			ResultSet rs = getRegisteredClients.executeQuery();
			conn.commit();
			List<Integer> clients = new ArrayList<Integer>();
			while (rs.next()) {
				clients.add(rs.getInt(1));
			}
			return clients;
		} catch (SQLException e) {
			throw new GetRegisteredClientsException(e);
		}
	}

}
