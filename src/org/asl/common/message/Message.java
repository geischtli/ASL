package org.asl.common.message;

import java.io.Serializable;

import org.asl.common.message.types.MessageType;
import org.asl.middleware.database.SqlHandler;

/**
 * The abstract Message class gives a default behavior for all
 * messages sent in the system. There are three subclasses which
 * extend this class and are used to carry the specialized messages.
 * @author Sandro
 */
@SuppressWarnings("serial")
public abstract class Message implements Serializable {
	
	/**
	 * The type of the message.
	 */
	public MessageType type;
	
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
	 * Default Constructor,
	 */
	public Message() {
	}
	
	/**
	 * A Constructor of a message. Because we don't know yet the globally
	 * unique id of the message we set it to -1 to indicate this. Choose
	 * this constructor when it is totally new. 
	 */
	public Message(MessageType type, int local_id, int sender, int receiver, int queue,
			long startTime, String content, String action) {
		this.type = type;
		this.id = -1;
		this.local_id = local_id;
		this.sender = sender;
		this.receiver = receiver;
		this.queue = queue;
		this.startTime = startTime;
		this.content = content;
		this.action = action;
	}
	
	/**
	 * Just a useful way to output a message.
	 */
	@Override
	public String toString() {
		String m = "".concat(
				String.valueOf(type)).concat(",").concat(
				String.valueOf(id)).concat(",").concat(
				String.valueOf(local_id)).concat(",").concat(
				String.valueOf(sender)).concat(",").concat(
				String.valueOf(receiver)).concat(",").concat(
				String.valueOf(queue)).concat(",").concat(
				String.valueOf(startTime)).concat(",").concat(
				content);
		return m;
	}
	
	public abstract void processOnMiddleware();
	public abstract void processOnClient();
}
