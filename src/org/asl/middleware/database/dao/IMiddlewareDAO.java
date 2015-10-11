package org.asl.middleware.database.dao;

import org.asl.common.request.types.exceptions.RegisterMiddlewareException;
import org.asl.common.timing.TimeLogger;

public interface IMiddlewareDAO {
	
	public int registerMiddleware(TimeLogger timer, int requestId) throws RegisterMiddlewareException;
}
