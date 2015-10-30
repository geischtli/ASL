package org.asl.experiments.baselines.client.loadgenerator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;

import org.asl.client.AbstractClient;
import org.asl.common.request.Request.RequestType;
import org.asl.common.request.builder.RequestBuilder;
import org.asl.common.request.serialize.ByteBufferWrapper;
import org.asl.common.request.serialize.SerializingUtilities;
import org.asl.common.socket.SocketHelper;

public class BenchClient extends AbstractClient {

	private int reqCount;
	private long startTime;
	BufferedWriter logWriter;
	int totalRequests;
	
	private final class WriteCompletionHandler implements CompletionHandler<Integer, Integer> {
		
		private ByteBufferWrapper outbufWrap;
		
		private WriteCompletionHandler(ByteBufferWrapper outbufWrap) {
			this.outbufWrap = outbufWrap;
		}
		
		@Override
		public void completed(Integer writtenBytes, Integer expectedWriteBytes) {
			while (!writtenBytes.equals(expectedWriteBytes)) {
				try {
					writtenBytes += sc.write(outbufWrap.getBuf()).get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
			if (reqCount + 1 == requestList.size()) {
				try {
					System.out.println("I quit");
					double sec = (double)(System.nanoTime() - startTime)/1000000000.0;
					logWriter.write(String.valueOf((double)(totalRequests)/sec));
					logWriter.newLine();
					logWriter.flush();
					sc.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
			ByteBuffer inbuf = ByteBuffer.allocate(10);
			try {
				sc.read(inbuf).get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			ByteBufferWrapper outbufWrap = SerializingUtilities.packRequest(RequestBuilder.getRequest(requestList.get(reqCount), ci));
			reqCount++;
			sc.write(outbufWrap.getBuf(), outbufWrap.getBytes(), new WriteCompletionHandler(outbufWrap));
		}

		@Override
		public void failed(Throwable exc, Integer expectedWriteBytes) {
			System.out.println("write failed");
		}
	}

	public BenchClient(int port, String ip, BufferedWriter logWriter, int totalRequests) throws IOException {
		super(port, ip);
		this.reqCount = 0;
		this.startTime = 0;
		this.logWriter = logWriter;
		this.totalRequests = totalRequests;
		gatherRequests();
	}
	
	public void gatherRequests() {
		RequestBuilder.addRequestTypes(
				requestList,
				new RequestType[] {
						RequestType.SEND_MESSAGE
						},
				totalRequests
				);
	}
	
	@Override
	public void run() {
		sc = SocketHelper.openSocket();
		try {
			startTime = System.nanoTime();
			sc.connect(new InetSocketAddress(InetAddress.getByName(AbstractClient.ip), AbstractClient.port), null,
					new CompletionHandler<Void, Object>() {

						@Override
						public void completed(Void result, Object attachment) {
							ByteBufferWrapper outbufWrap = SerializingUtilities.packRequest(RequestBuilder.getRequest(requestList.get(reqCount), ci));
							sc.write(outbufWrap.getBuf(), outbufWrap.getBytes(), new WriteCompletionHandler(outbufWrap));
						}

						@Override
						public void failed(Throwable exc, Object attachment) {
							System.out.println("connect failed");
						}
				
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
