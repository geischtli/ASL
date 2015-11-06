//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

package org.asl.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.asl.client.completionHandlers.ConnectCompletionHandler;
import org.asl.common.request.Request.RequestType;
import org.asl.common.request.builder.RequestBuilder;
import org.asl.common.socket.SocketHelper;

public class VirtualClient extends AbstractClient {

	public VirtualClient(int port, String ip, int contentLength) throws IOException {
		super(port, ip, contentLength);
		gatherRequests();
	}
	
	public void gatherRequests() {
		RequestBuilder.addRequestTypes(
				requestList,
				new RequestType[] {
						RequestType.HANDSHAKE,
						RequestType.CREATE_QUEUE
						},
				1
				);
		RequestBuilder.addRequestTypes(
				requestList,
				new RequestType[] {
						//RequestType.GET_REGISTERED_CLIENTS,
						//RequestType.GET_REGISTERED_QUEUES,
						RequestType.SEND_MESSAGE,
						RequestType.GET_QUEUES_WITH_MESSAGES_FOR_CLIENT,
						RequestType.READ_ALL_MESSAGES_OF_QUEUE,
						RequestType.READ_MESSAGE_FROM_SENDER,
						RequestType.REMOVE_TOP_MESSAGE_FROM_QUEUE,
						},
				1000
				);
	}
	
	@Override
	public void run() {
		sc = SocketHelper.openSocket();
		try {
			sc.connect(new InetSocketAddress(InetAddress.getByName(AbstractClient.ip), AbstractClient.port), null,
		//	sc.connect(new InetSocketAddress(InetAddress.getLoopbackAddress(), AbstractClient.port), null,
					ConnectCompletionHandler.create(ci, sc, requestList, 0)
				);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		/*try {
			Thread.sleep(1000*360);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		try {
			ci.tpWriter.flush();
			ci.tpWriter.close();
			ci.rttWriter.flush();
			ci.rttWriter.close();
			System.out.println("closed loggers ugh");
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
	public void shutdown() {
		
	}
	
}
