//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

package org.asl.common.request.types.exceptions;

public class GetRegisteredQueuesException extends ASLException {

	private static final long serialVersionUID = 413L;
	
	public GetRegisteredQueuesException() {
		super();
	}
	
	public GetRegisteredQueuesException(Exception e) {
		super(e);
	}
	
}
