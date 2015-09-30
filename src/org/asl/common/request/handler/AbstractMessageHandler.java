package org.asl.common.request.handler;

import java.sql.Connection;

import org.asl.common.request.Request;
import org.asl.middleware.database.SqlHandler;

public abstract class AbstractMessageHandler  {
	
	protected final Request msg;
	protected final SqlHandler sqlHandler;
	
	public AbstractMessageHandler(Request msg, Connection conn) {
		this.msg = msg;
		this.sqlHandler = SqlHandler.getHandler(conn, msg);
	}
}