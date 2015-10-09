package org.asl.common.request;

import java.io.Serializable;

import org.asl.client.ClientInfo;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.timing.middleware.MiddlewareTimer;

/**
 * The abstract Message class gives a default behavior for all
 * messages sent in the system. There are three subclasses which
 * extend this class and are used to carry the specialized messages.
 * @author Sandro
 */
public abstract class Request implements Serializable {
	
	private static final long serialVersionUID = 101L;
	protected ASLException exception;
	
	public ASLException getException() {
		return exception;
	}

	protected void setException(ASLException e) {
		this.exception = e;
	}
	
	public enum RequestType {
		CREATE_QUEUE,
		DELETE_QUEUE,
		GET_QUEUES_WITH_MESSAGES_FOR_CLIENT,
		GET_REGISTERED_CLIENTS,
		HANDSHAKE,
		READ_ALL_MESSAGES_OF_QUEUE,
		READ_MESSAGE_FROM_SENDER,
		REMOVE_TOP_MESSAGE_FROM_QUEUE,
		SEND_MESSAGE,
		REGISTER_MIDDLEWARE
	}
	
	public abstract void processOnMiddleware(MiddlewareTimer timer, int reqCount);
	public abstract void processOnClient(ClientInfo ci) throws ASLException;
}
