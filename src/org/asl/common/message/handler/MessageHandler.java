package org.asl.common.message.handler;

import java.sql.Connection;

import org.asl.common.message.Message;

public class MessageHandler extends AbstractMessageHandler implements Runnable {
	
	public MessageHandler(Message msg, Connection conn) {
		super(msg, conn);
	}
	
	public static MessageHandler getHandler(Message msg, Connection conn) {
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
