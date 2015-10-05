package org.asl.middleware;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.asl.common.request.Request;
import org.asl.common.request.serialize.SerializingUtilities;
import org.asl.middleware.clientsession.ClientSession;

public class Middleware extends AbstractMiddleware {
	
	public Middleware(int port, boolean initDB) throws IOException, SQLException {
		super(port, initDB);
	}
	
	@Override
	public void accept() {
		serverChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {

			@Override
			public void completed(AsynchronousSocketChannel sc, Object att) {
				serverChannel.accept(null, this);
				ByteBuffer inbuf = ByteBuffer.allocate(10240);
				sc.read(inbuf, inbuf, new CompletionHandler<Integer, ByteBuffer>() {
					
					@Override
					public void completed(Integer len, ByteBuffer buf) {
						SerializingUtilities.unpackLength(inbuf);
						Request req = null;
						while (true) {
							if (SerializingUtilities.allBytesRead(len)) {
								req = SerializingUtilities.unpackRequest(inbuf, len);
								break;
							} else {
								int forcedBytesRead = SerializingUtilities.forceRead(inbuf, sc);
								len += forcedBytesRead;
							}
						}
						req.processOnMiddleware();
						ByteBuffer outbuf = ByteBuffer.wrap(SerializingUtilities.objectToByteArray(req));
						sc.write(outbuf);
						try {
							sc.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void failed(Throwable exc, ByteBuffer buf) {
						System.out.println("Reading problem: " + exc.getMessage());
						exc.printStackTrace();
						try {
							sc.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
			}
			
			@Override
			public void failed(Throwable exc, Object attachment) {
				System.out.println("Accepting problem: " + exc);
				exc.printStackTrace();
			}
		});
	}
}