package org.asl.middleware.database.model;

public class MessageTable {

	private final int id;
	private final int sender;
	private final int receiver;
	private final int queue;
	private final String msg;
	
	public MessageTable(int id, int sender, int receiver, int queue, String msg) {
		this.id = id;
		this.sender = sender;
		this.receiver = receiver;
		this.queue = queue;
		this.msg = msg;
	}
}