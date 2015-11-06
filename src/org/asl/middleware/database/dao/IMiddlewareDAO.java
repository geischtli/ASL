//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

package org.asl.middleware.database.dao;

import org.asl.common.request.types.exceptions.RegisterMiddlewareException;
import org.asl.middleware.MiddlewareInfo;

public interface IMiddlewareDAO {
	
	public int registerMiddleware(MiddlewareInfo mi) throws RegisterMiddlewareException;
}
