package org.asl.middleware.database.dao;

import org.asl.common.request.types.exceptions.RegisterMiddlewareException;
import org.asl.common.timer.middleware.MiddlewareTimer;

public interface IMiddlewareDAO {
	
	public int registerMiddleware(MiddlewareTimer timer, int requestId) throws RegisterMiddlewareException;
}
