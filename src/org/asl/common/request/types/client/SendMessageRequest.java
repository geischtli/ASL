//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

package org.asl.common.request.types.client;

import java.util.Date;

import org.asl.client.ClientInfo;
import org.asl.common.dateTuple.DateTriple;
import org.asl.common.request.Request;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.request.types.exceptions.SendMessageException;
import org.asl.middleware.MiddlewareInfo;
import org.asl.middleware.database.dao.impl.MessageDAO;

public class SendMessageRequest extends Request {
	
	private static final long serialVersionUID = 110L;
	private final int sender;
	private final int receiver;
	private final int queue;
	private final String content;
	private Date sendTime;
	private Date arrivalTime;
	private Date returnTime;
	
	public SendMessageRequest(int sender, int receiver, int queue, String content, int requestId) {
		super(sender, requestId);
		this.sender = sender;
		this.receiver = receiver;
		this.queue = queue;
		this.content = content;
		this.exception = new SendMessageException();
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
	
	public String getMessage() {
		return content;
	}
	
	public Date getArrivalTime() {
		return arrivalTime;
	}
	
	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	
	public Date getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}
	
	public Date getSendTime() {
		return sendTime;
	}
	
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	
	@Override
	public void processOnMiddleware(MiddlewareInfo mi) {
		try {
			DateTriple timings = MessageDAO.getMessageDAO().sendMessage(sender, receiver, queue, content, requestId, mi);
			setSendTime(timings.sendTime);
			setArrivalTime(timings.arrivalTime);
			setReturnTime(timings.returnTime);
		} catch (SendMessageException e) {
			setException(e);
		}
	}

	@Override
	public void processOnClient(ClientInfo ci) throws ASLException {
		if (!getException().carriesError()) {
			//System.out.println("Message successfully sent");
		} else {
			throw getException();
		}
	}

}
