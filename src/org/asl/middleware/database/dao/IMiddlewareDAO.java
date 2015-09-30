package org.asl.middleware.database.dao;

import org.asl.common.message.types.exceptions.RegisterMiddlewareException;

public interface IMiddlewareDAO {
	
	public int registerMiddleware() throws RegisterMiddlewareException;
}
