//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

package org.asl.common.request.types.exceptions;

public class GetNumberOfMessagesException extends ASLException {

	private static final long serialVersionUID = 501L;
	
	public GetNumberOfMessagesException() {
		super();
	}
	
	public GetNumberOfMessagesException(Exception e) {
		super(e);
	}
	
}