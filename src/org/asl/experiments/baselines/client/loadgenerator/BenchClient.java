package org.asl.experiments.baselines.client.loadgenerator;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.asl.client.AbstractClient;
import org.asl.common.request.Request.RequestType;
import org.asl.common.request.builder.RequestBuilder;
import org.asl.common.request.serialize.ByteBufferWrapper;
import org.asl.common.request.serialize.SerializingUtilities;
import org.asl.common.socket.SocketHelper;

public class BenchClient extends AbstractClient {

	public BenchClient(int port, String ip) throws IOException {
		super(port, ip);
		gatherRequests();
	}
	
	public void gatherRequests() {
		RequestBuilder.addRequestTypes(
				requestList,
				new RequestType[] {
						RequestType.SEND_MESSAGE
						},
				1000
				);
	}
	
	@Override
	public void run() {
		sc = SocketHelper.openSocket();
		try {
			Future<Void> f = sc.connect(new InetSocketAddress(InetAddress.getByName(AbstractClient.ip), AbstractClient.port));
			f.get();
			for (int i = 0; i < requestList.size(); i++) {
				ByteBufferWrapper outbufWrap = SerializingUtilities.packRequest(RequestBuilder.getRequest(requestList.get(ci.getRequestId()), ci));
				Integer result = 0;
				while (result < outbufWrap.getBytes()) {
					Future<Integer> writefuture = sc.write(outbufWrap.getBuf());
					result += writefuture.get();
				}
			}
		} catch (UnknownHostException | InterruptedException | ExecutionException e1) {
			e1.printStackTrace();
		}
	}

	
	
}
