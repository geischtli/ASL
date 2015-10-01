package org.asl.common.request.types.exceptions;

@SuppressWarnings("serial")
public class GetQueuesWithMessagesForClientException extends ASLException {

	public GetQueuesWithMessagesForClientException() {
		super();
	}
	
	public GetQueuesWithMessagesForClientException(Exception e) {
		super(e);
	}
}
