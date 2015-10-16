package org.asl.common.request.types.middleware;

import org.asl.client.ClientInfo;
import org.asl.common.request.Request;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.request.types.exceptions.RegisterMiddlewareException;
import org.asl.middleware.MiddlewareInfo;
import org.asl.middleware.database.dao.impl.MiddlewareDAO;

public class RegisterMiddlewareRequest extends Request {

	private static final long serialVersionUID = 111L;
	
	@Override
	public void processOnMiddleware(MiddlewareInfo mi) {
		try {
			// ignore timing on this one, because it is anyway only called once and would need
			// extra handling in code, so we take "bad" way instead of correct but costly one.
			// so timer==null and requestId=0
			mi.setMiddlewareId(MiddlewareDAO.getMiddlewareDAO().registerMiddleware(mi));
			System.out.println("Middleware registered with ID " + mi.getMiddlewareId());
		} catch (RegisterMiddlewareException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void processOnClient(ClientInfo ci) throws ASLException {
		// will never be handled on client side
	}

}
