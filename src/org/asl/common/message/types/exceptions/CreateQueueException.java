package org.asl.common.message.types.exceptions;

@SuppressWarnings("serial")
public class CreateQueueException extends ASLException {

	public CreateQueueException() {
		super();
	}
	
	public CreateQueueException(Exception e) {
		super(e);
	}
}
