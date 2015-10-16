package org.asl.middleware.database.dao.impl;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import org.asl.common.request.types.exceptions.RegisterMiddlewareException;
import org.asl.middleware.MiddlewareInfo;
import org.asl.middleware.database.config.ASLDatabase;
import org.asl.middleware.database.connectionpool.ConnectionWrapper;
import org.asl.middleware.database.dao.IMiddlewareDAO;
import org.asl.middleware.database.dao.common.CommonDAO;
import org.asl.middleware.database.model.MiddlewareSequence;

public class MiddlewareDAO implements IMiddlewareDAO {

	public static MiddlewareDAO getMiddlewareDAO() {
		return new MiddlewareDAO();
	}
	
	@Override
	public int registerMiddleware(MiddlewareInfo mi) throws RegisterMiddlewareException {
		try (ConnectionWrapper conn = ASLDatabase.getNewConnection().get()) {
			PreparedStatement registerMiddleware = conn.get().prepareStatement(MiddlewareSequence.REGISTER_MIDDLEWARE_STRING);
			ResultSet rs = CommonDAO.executeQuery(conn.get(), registerMiddleware, -2, -1, mi);
			rs.next();
			return rs.getInt(1);
		} catch (SQLException | IOException | InterruptedException | ExecutionException e) {
			throw new RegisterMiddlewareException(e);
		}
	}
	
}
