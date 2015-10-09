package org.asl.middleware.database.dao;

import org.asl.common.request.types.exceptions.RegisterMiddlewareException;
import org.asl.common.timing.ASLTimer;

public interface IMiddlewareDAO {
	
	public int registerMiddleware(ASLTimer timer, int requestId) throws RegisterMiddlewareException;
}
