package org.asl.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.asl.client.completionHandlers.ConnectCompletionHandler;
import org.asl.common.request.Request.RequestType;
import org.asl.common.request.builder.RequestBuilder;
import org.asl.common.socket.SocketHelper;

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
						RequestType.CREATE_QUEUE
						},
				1
				);
		RequestBuilder.addRequestTypes(
				requestList,
				new RequestType[]{
						RequestType.GET_REGISTERED_CLIENTS,
						RequestType.SEND_MESSAGE
						},
				200
				);
	}
	
	@Override
	public void run() {
		sc = SocketHelper.openSocket();
		sc.connect(new InetSocketAddress(InetAddress.getLoopbackAddress(), AbstractClient.port), null,
				ConnectCompletionHandler.create(ci, sc, requestList, 0)
			);
	}
	
}
