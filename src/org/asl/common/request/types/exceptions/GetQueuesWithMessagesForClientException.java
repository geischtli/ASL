package org.asl.common.request.types.exceptions;

public class GetQueuesWithMessagesForClientException extends ASLException {

	private static final long serialVersionUID = 404L;
	
	public GetQueuesWithMessagesForClientException() {
		super();
	}
	
	public GetQueuesWithMessagesForClientException(Exception e) {
		super(e);
	}
}
