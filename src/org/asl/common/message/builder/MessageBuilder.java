package org.asl.common.message.builder;

import org.asl.common.message.Message;
import org.asl.common.message.types.ContentMessage;
import org.asl.common.message.types.MessageType;
import org.asl.common.message.types.SqlMessage;
import org.asl.common.message.types.StatusMessage;
import org.asl.common.message.types.client.CreateQueueMessage;
import org.asl.common.message.types.client.HandshakeMessage;
import org.asl.common.message.types.middleware.RegisterMiddlewareMessage;

/**
 * Builds up the message by setting the values of the fields.
 * This allows to make chaining method calls which are only
 * used to set a specific field of the message to be built.
 * @author Sandro
 *
 */
public class MessageBuilder {
	
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
	public MessageBuilder setID(int id) {
		this.id = id;
		return this;
	}
	
	/**
	 * Set local id.
	 */
	public MessageBuilder setLocalID(int local_id) {
		this.local_id = local_id;
		return this;
	}
	
	/**
	 * Set the id of the sending client.
	 */
	public MessageBuilder setSender(int sender) {
		this.sender = sender;
		return this;
	}
	
	/**
	 * Set the id of the receiving client.
	 */
	public MessageBuilder setReceiver(int receiver) {
		this.receiver = receiver;
		return this;
	}
	
	/**
	 * Set id of destinating queue.
	 */
	public MessageBuilder setQueue(int queue) {
		this.queue = queue;
		return this;
	}
	
	/**
	 * Set start time of the message.
	 */
	public MessageBuilder setStartTime(long startTime) {
		this.startTime = startTime;
		return this;
	}
	
	/**
	 * Set the content.
	 */
	public MessageBuilder setContent(String content) {
		this.content = content;
		return this;
	}
	
	/**
	 * Set the action.
	 */
	public MessageBuilder setAction(String action) {
		this.action = action;
		return this;
	}
	
	/**
	 * Build the message according to the type requested.
	 */
	public Message build(MessageType type) {
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
	public static MessageBuilder getBuilder() {
		return new MessageBuilder();
	}
	
	public static HandshakeMessage newHandshakeMessage() {
		return new HandshakeMessage(-1);
	}
	
	public static CreateQueueMessage newCreateQueueMessage() {
		return new CreateQueueMessage(-1);
	}
	
	public static RegisterMiddlewareMessage newRegisterMiddlewareMessage() {
		return new RegisterMiddlewareMessage();
	}
}

