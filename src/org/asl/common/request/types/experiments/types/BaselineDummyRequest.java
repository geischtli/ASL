//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

package org.asl.common.request.types.experiments.types;

import org.asl.client.ClientInfo;
import org.asl.common.request.Request;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.request.types.experiments.exceptions.BaselineDummyException;
import org.asl.middleware.MiddlewareInfo;
import org.asl.middleware.database.dao.impl.ExperimentDAO;

public class BaselineDummyRequest extends Request {

	private static final long serialVersionUID = 701L;

	public BaselineDummyRequest(int clientId, int requestId) {
		super(clientId, requestId);
		this.exception = new BaselineDummyException();
	}
	
	@Override
	public void processOnMiddleware(MiddlewareInfo mi) {
		try {
			ExperimentDAO.getExperimentDAO().baselineDummy(clientId, requestId, mi);
		} catch (BaselineDummyException e) {
			setException(e);
		}
	}

	@Override
	public void processOnClient(ClientInfo ci) throws ASLException {
		// nothing to do here
	}

}
