package org.asl.common.request.types.exceptions;

@SuppressWarnings("serial")
public class RemoveTopMessageFromQueueException extends ASLException {

	public RemoveTopMessageFromQueueException() {
		super();
	}
	
	public RemoveTopMessageFromQueueException(Exception e) {
		super(e);
	}
	
}
