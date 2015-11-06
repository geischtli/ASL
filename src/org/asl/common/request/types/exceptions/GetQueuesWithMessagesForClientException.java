//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

package org.asl.common.request.types.exceptions;

public class GetQueuesWithMessagesForClientException extends ASLException {

	private static final long serialVersionUID = 404L;
	
	public GetQueuesWithMessagesForClientException() {
		super();
	}
	
	public GetQueuesWithMessagesForClientException(Exception e) {
		super(e);
	}
}
