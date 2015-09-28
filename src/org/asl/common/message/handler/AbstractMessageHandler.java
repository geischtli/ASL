package org.asl.common.message.handler;

import java.sql.Connection;

import org.asl.common.message.Message;
import org.asl.middleware.database.SqlHandler;

public abstract class AbstractMessageHandler  {
	
	protected final Message msg;
	protected final SqlHandler sqlHandler;
	
	public AbstractMessageHandler(Message msg, Connection conn) {
		this.msg = msg;
		this.sqlHandler = SqlHandler.getHandler(conn, msg);
	}
}