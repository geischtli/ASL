package org.asl.common.request.serialize;

import java.nio.ByteBuffer;

public class ByteBufferWrapper {

	private final ByteBuffer buf;
	private final int bytes;
	
	public ByteBufferWrapper(ByteBuffer buf, int bytes) {
		this.buf = buf;
		this.bytes = bytes;
	}
	
	public static ByteBufferWrapper create(ByteBuffer buf, int bytes) {
		return new ByteBufferWrapper(buf, bytes);
	}
	
	public ByteBuffer getBuf() {
		return buf;
	}
	
	public int getBytes() {
		return bytes;
	}
	
}
