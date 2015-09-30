package org.asl.middleware.database.dao;

import org.asl.common.request.types.exceptions.RegisterMiddlewareException;

public interface IMiddlewareDAO {
	
	public int registerMiddleware() throws RegisterMiddlewareException;
}
