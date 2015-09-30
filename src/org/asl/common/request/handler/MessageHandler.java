package org.asl.common.request.handler;

import java.sql.Connection;

import org.asl.common.request.Request;

public class MessageHandler extends AbstractMessageHandler implements Runnable {
	
	public MessageHandler(Request msg, Connection conn) {
		super(msg, conn);
	}
	
	public static MessageHandler getHandler(Request msg, Connection conn) {
		return new MessageHandler(msg, conn);
	}
	
	@Override
	public void run() {
		switch (msg.type) {
		case CONTENT:
		case SQL:
			sqlHandler.processMessage();
			break;
		case STATUS:
			// ignore for now TODO
			break;
		default:
			break;
			
		}
	}
}
