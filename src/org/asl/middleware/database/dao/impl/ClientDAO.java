package org.asl.middleware.database.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.asl.middleware.database.config.ASLDatabase;
import org.asl.middleware.database.dao.IClientDAO;
import org.asl.middleware.database.model.ClientSequence;

public class ClientDAO implements IClientDAO {

	public static ClientDAO getClientDAO() {
		return new ClientDAO();
	}
	
	@Override
	public int registerClient() {
		try (Connection conn = ASLDatabase.getNewConnection()) {
			PreparedStatement register_client = conn.prepareStatement(ClientSequence.REGISTER_CLIENT_STRING);
			ResultSet rs = register_client.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

}
