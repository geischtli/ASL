package org.asl.client.completionHandlers;

import java.nio.channels.CompletionHandler;

import org.asl.common.request.Request;
import org.asl.common.request.serialize.ByteBufferWrapper;
import org.asl.common.request.serialize.SerializingUtilities;

public class ConnectCompletionHandler implements CompletionHandler<Void, Object> {

	private Request req;
	
	public ConnectCompletionHandler(Request req) {
		this.req = req;
	}
	
	public static ConnectCompletionHandler create(Request req) {
		return new ConnectCompletionHandler(req);
	}
	
	@Override
	public void completed(Void result, Object attachment) {
		ByteBufferWrapper outbufWrap = SerializingUtilities.packRequest(req);
	}

	@Override
	public void failed(Throwable exc, Object attachment) {
		
	}

}
