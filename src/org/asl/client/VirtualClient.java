package org.asl.client;

import java.io.IOException;

import org.asl.common.request.Request.RequestType;
import org.asl.common.request.builder.RequestBuilder;

public class VirtualClient extends AbstractClient {

	public VirtualClient(int port) throws IOException {
		super(port);
		gatherRequests();
	}
	
	public void gatherRequests() {
		RequestBuilder.addRequestTypes(
				requestList,
				new RequestType[]{
						RequestType.HANDSHAKE,
						//RequestType.CREATE_QUEUE
						},
				1
				);
		RequestBuilder.addRequestTypes(
				requestList,
				new RequestType[]{
						RequestType.GET_REGISTERED_CLIENTS,
						RequestType.SEND_MESSAGE
						},
				0
				);
	}
	
	@Override
	public void run() {
		
	}
	
}
