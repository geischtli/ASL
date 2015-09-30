package org.asl.middleware.database.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.asl.common.message.types.exceptions.RegisterMiddlewareException;
import org.asl.middleware.database.config.ASLDatabase;
import org.asl.middleware.database.dao.IMiddlewareDAO;
import org.asl.middleware.database.model.MiddlewareSequence;

public class MiddlewareDAO implements IMiddlewareDAO {

	public static MiddlewareDAO getMiddlewareDAO() {
		return new MiddlewareDAO();
	}
	
	@Override
	public int registerMiddleware() throws RegisterMiddlewareException {
		try (Connection conn = ASLDatabase.getNewConnection()) {
			PreparedStatement register_middleware = conn.prepareStatement(MiddlewareSequence.REGISTER_MIDDLEWARE_STRING);
			ResultSet rs = register_middleware.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			throw new RegisterMiddlewareException(e);
		}
	}
	
}
