package org.asl.common.request.builder;

import org.asl.common.request.Request;
import org.asl.common.request.types.ContentMessage;
import org.asl.common.request.types.MessageType;
import org.asl.common.request.types.SqlMessage;
import org.asl.common.request.types.StatusMessage;
import org.asl.common.request.types.client.CreateQueueRequest;
import org.asl.common.request.types.client.HandshakeRequest;
import org.asl.common.request.types.middleware.RegisterMiddlewareRequest;

/**
 * Builds up the message by setting the values of the fields.
 * This allows to make chaining method calls which are only
 * used to set a specific field of the message to be built.
 * @author Sandro
 *
 */
public class RequestBuilder {
	
	/**
	 * The globally unique message id. This will onyl be known as soon as
	 * the message enteres the database. There a counter will increment
	 * for every incoming message.
	 */
	public int id;
	
	/**
	 * The local id within a client. This is used to allow the client to
	 * have multiple messages in the system which all have not yet received
	 * a globally unique id from the database. The clients will have to do
	 * some bookkeeping to see which global id's arrive for which local id's.
	 */
	public int local_id;
	
	/**
	 * The id of the sender client.
	 */
	public int sender;
	
	/**
	 * The id of the receiver. Either this field is > 0, i.e. a valid
	 * id, or it is 0, indicating this is a message outgoing to every
	 * client.
	 * TODO: Check if this is really the case: Is is a broadcast?
	 */
	public int receiver;
	
	/**
	 * The id of the queue. Every queue in the system has a globally unique
	 * id, which allows the sender address it by embedding the id in the
	 * message.
	 */
	public int queue;
	
	/**
	 * This field indicates the time in milliseconds the message was sent
	 * from the client into the system. 
	 */
	public long startTime;
	
	/**
	 * Actual content of the message.
	 */
	public String content;
	
	/**
	 * The action this messages implies to do. Like insert into table,
	 * create/drop table.
	 */
	public String action;
	
	/**
	 * Set the message id.
	 */
	public RequestBuilder setID(int id) {
		this.id = id;
		return this;
	}
	
	/**
	 * Set local id.
	 */
	public RequestBuilder setLocalID(int local_id) {
		this.local_id = local_id;
		return this;
	}
	
	/**
	 * Set the id of the sending client.
	 */
	public RequestBuilder setSender(int sender) {
		this.sender = sender;
		return this;
	}
	
	/**
	 * Set the id of the receiving client.
	 */
	public RequestBuilder setReceiver(int receiver) {
		this.receiver = receiver;
		return this;
	}
	
	/**
	 * Set id of destinating queue.
	 */
	public RequestBuilder setQueue(int queue) {
		this.queue = queue;
		return this;
	}
	
	/**
	 * Set start time of the message.
	 */
	public RequestBuilder setStartTime(long startTime) {
		this.startTime = startTime;
		return this;
	}
	
	/**
	 * Set the content.
	 */
	public RequestBuilder setContent(String content) {
		this.content = content;
		return this;
	}
	
	/**
	 * Set the action.
	 */
	public RequestBuilder setAction(String action) {
		this.action = action;
		return this;
	}
	
	/**
	 * Build the message according to the type requested.
	 */
	public Request build(MessageType type) {
		switch(type) {
		case CONTENT:
			return new ContentMessage(
					MessageType.CONTENT,
					local_id,
					sender,
					receiver,
					queue,
					startTime,
					content,
					action
					);
		case SQL:
			return new SqlMessage(
					MessageType.CONTENT,
					local_id,
					sender,
					receiver,
					queue,
					startTime,
					content,
					action
					);
		case STATUS:
			return new StatusMessage(
					MessageType.CONTENT,
					local_id,
					sender,
					receiver,
					queue,
					startTime,
					content,
					action
					);
			default:
				return null;
		}
	}
	
	/**
	 * Don't force me to write a new expression in the client code
	 * of this builder.
	 */
	public static RequestBuilder getBuilder() {
		return new RequestBuilder();
	}
	
	public static HandshakeRequest newHandshakeRequest() {
		return new HandshakeRequest(-1);
	}
	
	public static CreateQueueRequest newCreateQueueRequest() {
		return new CreateQueueRequest(-1);
	}
	
	public static RegisterMiddlewareRequest newRegisterMiddlewareRequest() {
		return new RegisterMiddlewareRequest();
	}
}

