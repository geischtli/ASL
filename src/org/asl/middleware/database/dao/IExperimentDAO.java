package org.asl.middleware.database.dao;

import org.asl.common.request.types.experiments.exceptions.BaselineDummyException;
import org.asl.middleware.MiddlewareInfo;

public interface IExperimentDAO {

	public void baselineDummy(int clientId, int requestId, MiddlewareInfo mi) throws BaselineDummyException;
	
}