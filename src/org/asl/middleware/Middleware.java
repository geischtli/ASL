package org.asl.middleware;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.sql.SQLException;

import org.asl.common.request.Request;
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
				sc.read(inbuf, inbuf, new CompletionHandler<Integer, ByteBuffer>() {
					
					@Override
					public void completed(Integer readBytes, ByteBuffer buf) {
						ByteBuffer fullInbuf = serUtil.forceFurtherReadIfNeeded(inbuf, readBytes, sc);
						Request req = serUtil.unpackRequest(fullInbuf);
						req.processOnMiddleware();
						
						ByteBuffer outbuf = serUtil.packRequest(req);
						sc.write(outbuf, null, new CompletionHandler<Integer, Object>() {

							@Override
							public void completed(Integer writtenBytes, Object attachment) {
								serUtil.forceFurtherWriteIfNeeded(outbuf, writtenBytes, sc);
								SocketHelper.closeSocket(sc);
							}

							@Override
							public void failed(Throwable se, Object attachment) {
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
					public void failed(Throwable se, ByteBuffer buf) {
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