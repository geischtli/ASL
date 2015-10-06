package org.asl.middleware.database.dao.impl;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import org.asl.common.request.types.exceptions.RegisterMiddlewareException;
import org.asl.middleware.database.config.ASLDatabase;
import org.asl.middleware.database.connectionpool.ConnectionWrapper;
import org.asl.middleware.database.dao.IMiddlewareDAO;
import org.asl.middleware.database.model.MiddlewareSequence;

public class MiddlewareDAO implements IMiddlewareDAO {

	public static MiddlewareDAO getMiddlewareDAO() {
		return new MiddlewareDAO();
	}
	
	@Override
	public int registerMiddleware() throws RegisterMiddlewareException {
		try (ConnectionWrapper conn = ASLDatabase.getNewConnection().get()) {
			PreparedStatement register_middleware = conn.get().prepareStatement(MiddlewareSequence.REGISTER_MIDDLEWARE_STRING);
			ResultSet rs = register_middleware.executeQuery();
			rs.next();
			conn.get().commit();
			return rs.getInt(1);
		} catch (SQLException | IOException | InterruptedException | ExecutionException e) {
			throw new RegisterMiddlewareException(e);
		}
	}
	
}
