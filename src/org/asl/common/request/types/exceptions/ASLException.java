package org.asl.common.request.types.exceptions;

import java.io.Serializable;

public class ASLException extends Exception implements Serializable {

	private static final long serialVersionUID = 401L;
	protected boolean carriesError;
	
	public ASLException() {
		this.carriesError = false;
	}
	
	public ASLException(Exception e) {
		super(e);
		this.carriesError = true;
	}

	public boolean carriesError() {
		return carriesError;
	}

	public void carriesError(boolean carriesError) {
		this.carriesError = carriesError;
	}
}
