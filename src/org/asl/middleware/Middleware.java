package org.asl.middleware;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.sql.SQLException;

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
			public void completed(AsynchronousSocketChannel ssc, Object att) {
				System.out.println("accepted connection");
				serverChannel.accept(null, this);
				ClientSession session = new ClientSession(ssc, -1); //TODO: fix client_id = -1 -> send from client.
				final ByteBuffer buf = ByteBuffer.allocate(10240);
				ssc.read(buf, session, new CompletionHandler<Integer, ClientSession>() {
					
					@Override
					public void completed(Integer len, ClientSession cs) {
						boolean ret = cs.handleInput(buf, len);
						if (ret) {
							cs.getChannel().read(buf, cs, this);
						} else {
							cs.getRequest().processOnMiddleware();
							ByteBuffer outbuf = ByteBuffer.wrap(SerializingUtilities.objectToByteArray(cs.getRequest()));
							cs.getChannel().write(outbuf);
							try {
								cs.getChannel().close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}

					@Override
					public void failed(Throwable exc, ClientSession cs) {
						System.out.println("Reading problem: " + exc.getMessage());
						exc.printStackTrace();
						cs.handleFailure();
						try {
							cs.getChannel().close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				//serverChannel.accept(null, this);
			}
			
			@Override
			public void failed(Throwable exc, Object attachment) {
				System.out.println("Accepting problem: " + exc);
				exc.printStackTrace();
			}
		});
	}
}