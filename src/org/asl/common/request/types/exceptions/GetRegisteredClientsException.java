//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

package org.asl.common.request.types.exceptions;

public class GetRegisteredClientsException extends ASLException {

	private static final long serialVersionUID = 405L;
	
	public GetRegisteredClientsException() {
		super();
	}
	
	public GetRegisteredClientsException(Exception e) {
		super(e);
	}
	
}
