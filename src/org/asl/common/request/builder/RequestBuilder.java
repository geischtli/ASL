package org.asl.common.request.builder;

import java.util.Arrays;
import java.util.List;

import org.asl.client.ClientInfo;
import org.asl.common.request.Request;
import org.asl.common.request.Request.RequestType;
import org.asl.common.request.types.client.CreateQueueRequest;
import org.asl.common.request.types.client.DeleteQueueRequest;
import org.asl.common.request.types.client.GetQueuesWithMessagesForClientRequest;
import org.asl.common.request.types.client.GetRegisteredClientsRequest;
import org.asl.common.request.types.client.HandshakeRequest;
import org.asl.common.request.types.client.ReadAllMessagesOfQueueRequest;
import org.asl.common.request.types.client.ReadMessageFromSenderRequest;
import org.asl.common.request.types.client.RemoveTopMessageFromQueueRequest;
import org.asl.common.request.types.client.SendMessageRequest;
import org.asl.common.request.types.middleware.RegisterMiddlewareRequest;

/**
 * Builds up the message by setting the values of the fields.
 * This allows to make chaining method calls which are only
 * used to set a specific field of the message to be built.
 * @author Sandro
 *
 */
public class RequestBuilder {
	
	public static List<RequestType> addRequestTypes(List<RequestType> requestList, RequestType[] requests, int n) {
		for (int i = 0; i < n; i++) {
			requestList.addAll(Arrays.asList(requests));
		}
		return requestList;
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

