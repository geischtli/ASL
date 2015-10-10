package org.asl.middleware;

import java.io.IOException;
import java.sql.SQLException;

import org.asl.middleware.completionHandlers.AcceptCompletionHandler;

public class Middleware extends AbstractMiddleware {
	
	public Middleware(int port) throws IOException, SQLException {
		super(port);
	}
	
	@Override
	public void accept() {
		serverChannel.accept(null, AcceptCompletionHandler.create(serverChannel, watchDog, timer, requestId));
	}
	
	/*@Override
	public void accept() {
		serverChannel.accept(++requestId, new CompletionHandler<AsynchronousSocketChannel, Integer>() {

			@Override
			public void completed(AsynchronousSocketChannel sc, Integer requestId) {
				watchDog.addConnection(new ConnectionTimeWrapper(sc, System.nanoTime()));
//				timer.click(MiddlewareTimings.ACCEPTED_CLIENT, requestId);
				//accept();
				//serverChannel.accept(requestId, this);
				ByteBuffer inbuf = ByteBuffer.allocate(AbstractMiddleware.INITIAL_BUFSIZE);
				sc.read(inbuf, null, new CompletionHandler<Integer, Object>() {
					
					@Override
					public void completed(Integer readBytes, Object attachment) {
						ByteBufferWrapper fullInbufWrap = SerializingUtilities.forceFurtherReadIfNeeded(inbuf, readBytes, sc);
						
//						timer.click(MiddlewareTimings.READ_REQUEST, requestId);
						Request req = SerializingUtilities.unpackRequest(fullInbufWrap.getBuf(), fullInbufWrap.getBytes());
//						timer.click(MiddlewareTimings.PROCESSED_READ, requestId);
						
						req.processOnMiddleware(timer, requestId);
						
						ByteBufferWrapper outbufWrap = SerializingUtilities.packRequest(req);
//						timer.click(MiddlewareTimings.PACKED_REQUEST, requestId);
						sc.write(outbufWrap.getBuf(), this, new CompletionHandler<Integer, CompletionHandler<Integer, Object>>() {

							@Override
							public void completed(Integer writtenBytes, CompletionHandler<Integer, Object> readHandler) {
								SerializingUtilities.forceFurtherWriteIfNeeded(outbufWrap.getBuf(), writtenBytes, outbufWrap.getBytes(), sc);
//								timer.click(MiddlewareTimings.WROTE_ANSWER, requestId);
								inbuf.flip();
								sc.read(inbuf, null, readHandler);
							}
							
							@Override
							public void failed(Throwable se, CompletionHandler<Integer, Object> readHandler) {
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
	}*/
}