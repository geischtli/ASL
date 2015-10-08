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
	public void processOnMiddleware() {
		try {
			MiddlewareInfo.setMiddlewareId(MiddlewareDAO.getMiddlewareDAO().registerMiddleware());
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
