//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

package org.asl.common.request.types.exceptions;

public class RegisterMiddlewareException extends ASLException {

	private static final long serialVersionUID = 409L;
	
	public RegisterMiddlewareException() {
		super();
	}
	
	public RegisterMiddlewareException(Exception e) {
		super(e);
	}

}
