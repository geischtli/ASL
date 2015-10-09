package org.asl.middleware;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.sql.SQLException;

import org.asl.common.request.Request;
import org.asl.common.request.serialize.ByteBufferWrapper;
import org.asl.common.request.serialize.SerializingUtilities;
import org.asl.common.socket.SocketHelper;
import org.asl.common.socket.SocketLocation;
import org.asl.common.socket.SocketOperation;
import org.asl.common.timing.middleware.MiddlewareTimings;

public class Middleware extends AbstractMiddleware {
	
	public Middleware(int port) throws IOException, SQLException {
		super(port);
		int id = 313;
		String s = "wololo";
		long t = clock.takeTime(st -> pid -> heavyMethod(st, pid), s, id);
		System.out.println("This took " + t + " ns");
	}
	
	public void heavyMethod(String s, int id) {
		System.out.println("im in method and print string = " + s + " and id = " + id);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void accept() {
		serverChannel.accept(++requestId, new CompletionHandler<AsynchronousSocketChannel, Integer>() {

			@Override
			public void completed(AsynchronousSocketChannel sc, Integer requestId) {
				timer.click(MiddlewareTimings.ACCEPTED_CLIENT, requestId);
				accept();
				ByteBuffer inbuf = ByteBuffer.allocate(AbstractMiddleware.INITIAL_BUFSIZE);
				sc.read(inbuf, null, new CompletionHandler<Integer, Object>() {
					
					@Override
					public void completed(Integer readBytes, Object attachment) {
						ByteBufferWrapper fullInbufWrap = SerializingUtilities.forceFurtherReadIfNeeded(inbuf, readBytes, sc);
						
						timer.click(MiddlewareTimings.READ_REQUEST, requestId);
						Request req = SerializingUtilities.unpackRequest(fullInbufWrap.getBuf(), fullInbufWrap.getBytes());
						timer.click(MiddlewareTimings.PROCESSED_READ, requestId);
						
						req.processOnMiddleware(timer, requestId);
						
						ByteBufferWrapper outbufWrap = SerializingUtilities.packRequest(req);
						timer.click(MiddlewareTimings.PACKED_REQUEST, requestId);
						sc.write(outbufWrap.getBuf(), outbufWrap.getBytes(), new CompletionHandler<Integer, Integer>() {

							@Override
							public void completed(Integer writtenBytes, Integer expectedWriteBytes) {
								SerializingUtilities.forceFurtherWriteIfNeeded(outbufWrap.getBuf(), writtenBytes, expectedWriteBytes, sc);
								timer.click(MiddlewareTimings.WROTE_ANSWER, requestId);
								//timer.printSingleRequestTimings(requestId);
								SocketHelper.closeSocket(sc);
							}

							@Override
							public void failed(Throwable se, Integer expectedWriteBytes) {
								SocketHelper.closeSocketAfterException(
										SocketLocation.MIDDLEWARE,
										SocketOperation.WRITE,
										se,
										sc
									);
							}
							
						});
					}

					@Override
					public void failed(Throwable se, Object attachment) {
						SocketHelper.closeSocketAfterException(
								SocketLocation.MIDDLEWARE,
								SocketOperation.READ,
								se,
								sc
							);
					}
				});
			}
			
			@Override
			public void failed(Throwable se, Integer reqCounter) {
				SocketHelper.closeSocketAfterException(
						SocketLocation.MIDDLEWARE,
						SocketOperation.ACCEPT,
						se
					);
			}
		});
	}
}