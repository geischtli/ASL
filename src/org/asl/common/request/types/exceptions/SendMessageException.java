//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

package org.asl.common.request.types.exceptions;

public class SendMessageException extends ASLException {

	private static final long serialVersionUID = 411L;
	
	public SendMessageException() {
		super();
	}
	
	public SendMessageException(Exception e) {
		super(e);
	}
	
}
