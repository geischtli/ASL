package org.asl.common.request.types.exceptions;

@SuppressWarnings("serial")
public class ReadMessageFromSenderException extends ASLException {

	public ReadMessageFromSenderException() {
		super();
	}
	
	public ReadMessageFromSenderException(Exception e) {
		super(e);
	}
	
}
