package org.asl.middleware.database.model;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Message implements Serializable {
	
	private final int id;
	private final int sender;
	private final int receiver;
	private final int queue;
	private final String content;
	private final Date arrivalTime;
	
	public Message(int id, int sender, int receiver, int queue, String content, Date arrivalTime) {
		this.id = id;
		this.sender = sender;
		this.receiver = receiver;
		this.queue = queue;
		this.content = content;
		this.arrivalTime = arrivalTime;
	}
	
	public static Message getMessage(int id, int sender, int receiver, int queue, String content, Date arrivalTime) {
		return new Message(id, sender, receiver, queue, content, arrivalTime);
	}
	
	public int getId() {
		return id;
	}
	
	public int getSender() {
		return sender;
	}
	
	public int getReceiver() {
		return receiver;
	}
	
	public int getQueue() {
		return queue;
	}
	
	public String getContent() {
		return content;
	}
	
	public Date getArrivalTime() {
		return arrivalTime;
	}
	
}
