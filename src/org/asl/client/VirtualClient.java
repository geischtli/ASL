package org.asl.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Random;

import org.asl.client.completionHandlers.ConnectCompletionHandler;
import org.asl.common.request.Request.RequestType;
import org.asl.common.request.builder.RequestBuilder;
import org.asl.common.socket.SocketHelper;

public class VirtualClient extends AbstractClient {

	public static BufferedWriter logWriter;
	public static long startAll;
	public static long doneAll;
	
	public VirtualClient(int port, String ip) throws IOException {
		super(port, ip);
		gatherRequests();
		Random random = new Random();
		File myFile = null;
		do {
		int fileId = random.nextInt(Integer.MAX_VALUE);
		myFile = new File("/home/ec2-user/ASL/client_baseline/client"
				+ String.valueOf(fileId) + ".log");
		} while (myFile.exists());
		System.out.println("log file opened");
		VirtualClient.logWriter = new BufferedWriter(new FileWriter(myFile));
	}
	
	public static void writeLog(String s) {
		try {
			VirtualClient.logWriter.write(s);
			VirtualClient.logWriter.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void gatherRequests() {
		/*RequestBuilder.addRequestTypes(
				requestList,
				new RequestType[] {
						RequestType.HANDSHAKE,
						RequestType.CREATE_QUEUE
						},
				1
				);*/
		RequestBuilder.addRequestTypes(
				requestList,
				new RequestType[] {
					//	RequestType.GET_REGISTERED_CLIENTS,
					//	RequestType.GET_REGISTERED_QUEUES,
						RequestType.SEND_MESSAGE
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
	}
	
}
