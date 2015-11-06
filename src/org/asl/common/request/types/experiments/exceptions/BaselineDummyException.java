//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

package org.asl.common.request.types.experiments.exceptions;

import org.asl.common.request.types.exceptions.ASLException;

public class BaselineDummyException extends ASLException {
	
	private static final long serialVersionUID = 601L;
	
	public BaselineDummyException() {
		super();
	}
	
	public BaselineDummyException(Exception e) {
		super(e);
	}

}
