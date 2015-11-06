//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

package org.asl.common.request.types.exceptions;

public class RemoveTopMessageFromQueueException extends ASLException {

	private static final long serialVersionUID = 410L;
	
	public RemoveTopMessageFromQueueException() {
		super();
	}
	
	public RemoveTopMessageFromQueueException(Exception e) {
		super(e);
	}
	
}
