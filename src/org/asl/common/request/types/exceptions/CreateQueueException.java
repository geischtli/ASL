//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

package org.asl.common.request.types.exceptions;

public class CreateQueueException extends ASLException {

	private static final long serialVersionUID = 412L;
	
	public CreateQueueException() {
		super();
	}
	
	public CreateQueueException(Exception e) {
		super(e);
	}
}
