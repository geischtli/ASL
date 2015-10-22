package org.asl.common.request;

import java.io.Serializable;

import org.asl.client.ClientInfo;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.middleware.MiddlewareInfo;

/**
 * The abstract Message class gives a default behavior for all
 * messages sent in the system. There are three subclasses which
 * extend this class and are used to carry the specialized messages.
 * @author Sandro
 */
public abstract class Request implements Serializable {
	
	private static final long serialVersionUID = 101L;
	
	protected ASLException exception;
	// the 2 following fields provide a GLOBALLY UNIQUE request id tuple
	protected int clientId;
	protected int requestId;
	
	public Request() {}
	
	public Request(int clientId, int requestId) {
		this.clientId = clientId;
		this.requestId = requestId;
	}
	
	public ASLException getException() {
		return exception;
	}

	protected void setException(ASLException e) {
		this.exception = e;
	}

	public int getClientId() {
		return clientId;
	}
	
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	
	public int getRequestId() {
		return requestId;
	}
	
	public void setRequestId(int requestId) {
		this.requestId = requestId;
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
		REGISTER_MIDDLEWARE,
		GET_NUMBER_OF_MESSAGES,
		GET_REGISTERED_QUEUES,
		BASELINE_DUMMY
	}
	
	public abstract void processOnMiddleware(MiddlewareInfo mi);
	public abstract void processOnClient(ClientInfo ci) throws ASLException;
}
