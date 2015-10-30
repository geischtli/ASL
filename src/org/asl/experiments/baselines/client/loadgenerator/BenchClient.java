package org.asl.experiments.baselines.client.loadgenerator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.util.Timer;
import java.util.TimerTask;
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
	private Timer timer;
	private int reqPerSec;
	private long startWrite;
	private long startRead;
	private long totalWritePerSec;
	private long totalReadPerSec;
	
	private final class WriteCompletionHandler implements CompletionHandler<Integer, Integer> {
		
		private ByteBufferWrapper outbufWrap;
		
		private WriteCompletionHandler(ByteBufferWrapper outbufWrap) {
			this.outbufWrap = outbufWrap;
			startWrite = System.nanoTime();
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
			totalWritePerSec += (System.nanoTime() - startWrite);
			if (reqCount + 1 == requestList.size()) {
				try {
					System.out.println("I quit");
					timer.cancel();
					sc.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
			ByteBuffer inbuf = ByteBuffer.allocate(10);
			try {
				startRead = System.nanoTime();
				sc.read(inbuf).get();
				totalReadPerSec += (System.nanoTime() - startRead);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			ByteBufferWrapper outbufWrap = SerializingUtilities.packRequest(RequestBuilder.getRequest(requestList.get(reqCount), ci));
			reqCount++;
			reqPerSec++;
			sc.write(outbufWrap.getBuf(), outbufWrap.getBytes(), new WriteCompletionHandler(outbufWrap));
		}

		@Override
		public void failed(Throwable exc, Integer expectedWriteBytes) {
			System.out.println("write failed");
		}
	}

	public BenchClient(int port, String ip, final BufferedWriter logWriter, int totalRequests, final int id) throws IOException {
		super(port, ip);
		this.reqCount = 0;
		this.startTime = 0;
		this.logWriter = logWriter;
		this.totalRequests = totalRequests;
		this.timer = new Timer();
		this.reqPerSec = 0;
		this.timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
					// schema
					// id	requests/last second	[totalWriteTime in last second in ms]	[total read time in last second in ms]
					logWriter.write(String.valueOf(id) + "\t" + String.valueOf(reqPerSec)
					+ String.valueOf((double)totalWritePerSec/1000000.0) + "\t"
					+ String.valueOf((double)totalReadPerSec/1000000.0) + "\n");
					reqPerSec = 0;
					totalWritePerSec = 0;
					totalReadPerSec = 0;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 0, 1000);
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
