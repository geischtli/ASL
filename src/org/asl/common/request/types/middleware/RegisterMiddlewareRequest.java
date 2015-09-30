package org.asl.common.request.types.middleware;

import org.asl.common.request.Request;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.request.types.exceptions.RegisterMiddlewareException;
import org.asl.middleware.MiddlewareInfo;
import org.asl.middleware.database.dao.impl.MiddlewareDAO;

@SuppressWarnings("serial")
public class RegisterMiddlewareRequest extends Request {

	@Override
	public void processOnMiddleware() {
		try {
			MiddlewareInfo.setMiddlewareId(MiddlewareDAO.getMiddlewareDAO().registerMiddleware());
			System.out.println("Middleware registered with ID " + MiddlewareInfo.getMiddlewareId());
		} catch (RegisterMiddlewareException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void processOnClient() throws ASLException {
		// will never be handled on client side
	}

}
