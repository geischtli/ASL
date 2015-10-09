package org.asl.common.request.types.middleware;

import org.asl.client.ClientInfo;
import org.asl.common.request.Request;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.request.types.exceptions.RegisterMiddlewareException;
import org.asl.common.timing.middleware.MiddlewareTimer;
import org.asl.middleware.MiddlewareInfo;
import org.asl.middleware.database.dao.impl.MiddlewareDAO;

public class RegisterMiddlewareRequest extends Request {

	private static final long serialVersionUID = 111L;
	
	@Override
	public void processOnMiddleware(MiddlewareTimer timer, int requestId) {
		try {
			// ignore timing on this one, because it is anyway only called once and would need
			// extra handling in code, so we take "bad" way instead of correct but costly one.
			// so timer==null and requestId=0
			MiddlewareInfo.setMiddlewareId(MiddlewareDAO.getMiddlewareDAO().registerMiddleware(timer, requestId));
			System.out.println("Middleware registered with ID " + MiddlewareInfo.getMiddlewareId());
		} catch (RegisterMiddlewareException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void processOnClient(ClientInfo ci) throws ASLException {
		// will never be handled on client side
	}

}
