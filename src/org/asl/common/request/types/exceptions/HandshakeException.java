//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

package org.asl.common.request.types.exceptions;

public class HandshakeException extends ASLException {

	private static final long serialVersionUID = 406L;
	
	public HandshakeException() {
		super();
	}
	
	public HandshakeException(Exception e) {
		super(e);
	}

}
