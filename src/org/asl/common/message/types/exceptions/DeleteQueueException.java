package org.asl.common.message.types.exceptions;

@SuppressWarnings("serial")
public class DeleteQueueException extends ASLException {

	public DeleteQueueException() {
		super();
	}
	
	public DeleteQueueException(Exception e) {
		super(e);
	}
}