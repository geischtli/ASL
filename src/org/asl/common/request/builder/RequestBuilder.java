package org.asl.common.request.builder;

import java.util.Arrays;
import java.util.List;

import org.asl.client.ClientInfo;
import org.asl.common.request.Request;
import org.asl.common.request.Request.RequestType;
import org.asl.common.request.types.client.CreateQueueRequest;
import org.asl.common.request.types.client.DeleteQueueRequest;
import org.asl.common.request.types.client.GetNumberOfMessagesRequest;
import org.asl.common.request.types.client.GetQueuesWithMessagesForClientRequest;
import org.asl.common.request.types.client.GetRegisteredClientsRequest;
import org.asl.common.request.types.client.GetRegisteredQueuesRequest;
import org.asl.common.request.types.client.HandshakeRequest;
import org.asl.common.request.types.client.ReadAllMessagesOfQueueRequest;
import org.asl.common.request.types.client.ReadMessageFromSenderRequest;
import org.asl.common.request.types.client.RemoveTopMessageFromQueueRequest;
import org.asl.common.request.types.client.SendMessageRequest;
import org.asl.common.request.types.experiments.types.BaselineDummyRequest;
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
	
	public static Request getRegisterMiddlewareRequest() {
		return new RegisterMiddlewareRequest();
	}
	
	public static Request getRequest(RequestType type, ClientInfo ci) {
		int clientId = ci.getClientId();
		int requestId = ci.getRequestId();
		switch (type) {
			case CREATE_QUEUE:
				return new CreateQueueRequest(
						clientId,
						requestId
						);
			case DELETE_QUEUE:
				return new DeleteQueueRequest(
						ci.getDeleteQueueId(),
						ci.getClientId(),
						requestId
						);
			case GET_QUEUES_WITH_MESSAGES_FOR_CLIENT:
				return new GetQueuesWithMessagesForClientRequest(
						clientId,
						clientId,
						requestId
						);
			case GET_REGISTERED_CLIENTS:
				return new GetRegisteredClientsRequest(
						clientId,
						requestId
						);
			case HANDSHAKE:
				return new HandshakeRequest(
						clientId,
						requestId
						);
			case READ_ALL_MESSAGES_OF_QUEUE:
				return new ReadAllMessagesOfQueueRequest(
						ci.getReadQueueId(),
						clientId,
						clientId,
						requestId
						);
			case READ_MESSAGE_FROM_SENDER:
				return new ReadMessageFromSenderRequest(
						ci.getReadFromSenderId(),
						clientId,
						requestId
						);
			case REMOVE_TOP_MESSAGE_FROM_QUEUE:
				return new RemoveTopMessageFromQueueRequest(
						clientId,
						ci.getReadQueueId(),
						clientId,
						requestId
						);
			case SEND_MESSAGE:
				return new SendMessageRequest(
						clientId,
						ci.getSendReceiverId(),
						ci.getSendQueueId(),
						ci.getSendContentText(),
						requestId
						);
			case GET_REGISTERED_QUEUES:
				return new GetRegisteredQueuesRequest(
						clientId,
						requestId
						);
			case GET_NUMBER_OF_MESSAGES:
				return new GetNumberOfMessagesRequest(
						clientId,
						requestId
						);
			case BASELINE_DUMMY:
				return new BaselineDummyRequest(
						clientId,
						requestId
						);
			default:
				System.out.println("No request type found for " + type.toString());
				return null;
		}
		
	}
	
}

