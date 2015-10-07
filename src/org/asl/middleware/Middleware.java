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

public class Middleware extends AbstractMiddleware {
	
	public Middleware(int port) throws IOException, SQLException {
		super(port);
	}
	
	@Override
	public void accept() {
		serverChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {

			@Override
			public void completed(AsynchronousSocketChannel sc, Object att) {
				serverChannel.accept(null, this);
				ByteBuffer inbuf = ByteBuffer.allocate(AbstractMiddleware.INITIAL_BUFSIZE);
				sc.read(inbuf, null, new CompletionHandler<Integer, Object>() {
					
					@Override
					public void completed(Integer readBytes, Object attachment) {
						ByteBufferWrapper fullInbufWrap = SerializingUtilities.forceFurtherReadIfNeeded(inbuf, readBytes, sc);
						Request req = SerializingUtilities.unpackRequest(fullInbufWrap.getBuf(), fullInbufWrap.getBytes());
						req.processOnMiddleware();
						
						ByteBufferWrapper outbufWrap = SerializingUtilities.packRequest(req);
						sc.write(outbufWrap.getBuf(), outbufWrap.getBytes(), new CompletionHandler<Integer, Integer>() {

							@Override
							public void completed(Integer writtenBytes, Integer expectedWriteBytes) {
								SerializingUtilities.forceFurtherWriteIfNeeded(outbufWrap.getBuf(), writtenBytes, expectedWriteBytes, sc);
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
			public void failed(Throwable se, Object attachment) {
				SocketHelper.closeSocketAfterException(
						SocketLocation.MIDDLEWARE,
						SocketOperation.ACCEPT,
						se
					);
			}
		});
	}
}