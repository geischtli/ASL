package org.asl.middleware.database.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.asl.common.request.types.exceptions.HandshakeException;
import org.asl.middleware.MiddlewareInfo;
import org.asl.middleware.database.config.ASLDatabase;
import org.asl.middleware.database.dao.IClientDAO;
import org.asl.middleware.database.model.ClientTable;

public class ClientDAO implements IClientDAO {

	public static ClientDAO getClientDAO() {
		return new ClientDAO();
	}
	
	@Override
	public int registerClient() throws HandshakeException {
		try (Connection conn = ASLDatabase.getNewConnection()) {
			PreparedStatement register_client = conn.prepareStatement(ClientTable.REGISTER_CLIENT_STRING);
			register_client.setInt(1, MiddlewareInfo.getMiddlewareId());
			ResultSet rs = register_client.executeQuery();
			rs.next();
			conn.commit();
			return rs.getInt(1);
		} catch (SQLException e) {
			throw new HandshakeException(e);
		}
	}

}
