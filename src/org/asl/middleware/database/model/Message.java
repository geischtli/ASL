package org.asl.middleware.database.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Message implements Serializable {
	
	private final int id;
	private final int sender;
	private final int receiver;
	private final int queue;
	private final String content;
	
	public Message(int id, int sender, int receiver, int queue, String content) {
		this.id = id;
		this.sender = sender;
		this.receiver = receiver;
		this.queue = queue;
		this.content = content;
	}
	
	public static Message getMessage(int id, int sender, int receiver, int queue, String content) {
		return new Message(id, sender, receiver, queue, content);
	}
	
	public String getContent() {
		return content;
	}
	
}
