//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

package org.asl.common.request.types.exceptions;

public class ReadMessageFromSenderException extends ASLException {

	private static final long serialVersionUID = 408L;
	
	public ReadMessageFromSenderException() {
		super();
	}
	
	public ReadMessageFromSenderException(Exception e) {
		super(e);
	}
	
}
