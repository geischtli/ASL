package org.asl.middleware.database.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.asl.middleware.database.config.ASLDatabase;
import org.asl.middleware.database.dao.IClientDAO;

public class ClientDAO implements IClientDAO {

	@Override
	public void storeNewClient() {
		try (Connection conn = ASLDatabase.getNewConnection()) {
			Statement s = conn.createStatement();
			s.executeQuery("");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
