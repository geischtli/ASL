package org.asl.middleware.database.dao;

import org.asl.common.request.types.exceptions.RegisterMiddlewareException;
import org.asl.common.timing.TimeLogger;
import org.asl.middleware.MiddlewareInfo;

public interface IMiddlewareDAO {
	
	public int registerMiddleware(MiddlewareInfo mi) throws RegisterMiddlewareException;
}
