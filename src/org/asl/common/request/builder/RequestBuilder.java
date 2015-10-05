package org.asl.common.request.builder;

import org.asl.client.ClientInfo;
import org.asl.common.request.Request;
import org.asl.common.request.Request.RequestType;
import org.asl.common.request.types.ContentMessage;
import org.asl.common.request.types.MessageType;
import org.asl.common.request.types.SqlMessage;
import org.asl.common.request.types.StatusMessage;
import org.asl.common.request.types.client.CreateQueueRequest;
import org.asl.common.request.types.client.DeleteQueueRequest;
import org.asl.common.request.types.client.GetQueuesWithMessagesForClientRequest;
import org.asl.common.request.types.client.GetRegisteredClientsRequest;
import org.asl.common.request.types.client.HandshakeRequest;
import org.asl.common.request.types.client.ReadAllMessagesOfQueueRequest;
import org.asl.common.request.types.client.ReadMessageFromSenderRequest;
import org.asl.common.request.types.client.RemoveTopMessageFromQueueRequest;
import org.asl.common.request.types.client.SendMessageRequest;
import org.asl.common.request.types.exceptions.GetRegisteredClientsException;
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
	
	public static Request getRequest(RequestType type) {
		switch (type) {
			case CREATE_QUEUE:
				return new CreateQueueRequest(
						ClientInfo.getClientId()
						);
			case DELETE_QUEUE:
				return new DeleteQueueRequest(
						ClientInfo.getDeleteQueueId()
						);
			case GET_QUEUES_WITH_MESSAGES_FOR_CLIENT:
				return new GetQueuesWithMessagesForClientRequest(
						ClientInfo.getClientId()
						);
			case GET_REGISTERED_CLIENTS:
				return new GetRegisteredClientsRequest();
			case HANDSHAKE:
				return new HandshakeRequest();
			case READ_ALL_MESSAGES_OF_QUEUE:
				return new ReadAllMessagesOfQueueRequest(
						ClientInfo.getClientId(),
						ClientInfo.getReadQueueId()
						);
			case READ_MESSAGE_FROM_SENDER:
				return new ReadMessageFromSenderRequest(
						ClientInfo.getReadFromSenderId(),
						ClientInfo.getClientId()
						);
			case REMOVE_TOP_MESSAGE_FROM_QUEUE:
				return new RemoveTopMessageFromQueueRequest(
						ClientInfo.getClientId(),
						ClientInfo.getReadQueueId()
						);
			case SEND_MESSAGE:
				return new SendMessageRequest(
						ClientInfo.getClientId(),
						ClientInfo.getSendReceiverId(),
						ClientInfo.getSendQueueId(),
						ClientInfo.getSendContentText()
						);
			case REGISTER_MIDDLEWARE:
				return new RegisterMiddlewareRequest();
			default:
				System.out.println("No request type found for " + type.toString());
				return null;
		}
		
	}
	
	/*
	public static HandshakeRequest newHandshakeRequest() {
		return new HandshakeRequest(-1);
	}
	
	public static CreateQueueRequest newCreateQueueRequest(int creator_client) {
		return new CreateQueueRequest(creator_client);
	}
	
	public static DeleteQueueRequest newDeleteQueueRequest(int queue) {
		return new DeleteQueueRequest(queue);
	}
	
	public static GetRegisteredClientsRequest new GetRegisteredClientsRequest()
	
	public static SendMessageRequest newSendMessageRequest(int sender, int receiver, int queue, String content) {
		return new SendMessageRequest(sender, receiver, queue, content);
	}
	
	public static RegisterMiddlewareRequest newRegisterMiddlewareRequest() {
		return new RegisterMiddlewareRequest();
	}
	
	public static RemoveTopMessageFromQueueRequest newRemoveTopMessageFromQueueRequest(int receiver, int queue) {
		return new RemoveTopMessageFromQueueRequest(receiver, queue);
	}
	
	public static ReadAllMessagesOfQueueRequest newReadAllMessagesOfQueueRequest(int receiver, int queue) {
		return new ReadAllMessagesOfQueueRequest(receiver, queue);
	}
	
	public static ReadMessageFromSenderRequest newReadMessageFromSenderRequest(int sender, int receiver) {
		return new ReadMessageFromSenderRequest(sender, receiver);
	}
	
	public static GetQueuesWithMessagesForClientRequest newGetQueuesWithMessagesForClientRequest(int receiver) {
		return new GetQueuesWithMessagesForClientRequest(receiver);
	}*/
}

