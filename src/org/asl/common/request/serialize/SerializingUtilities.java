package org.asl.common.request.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.asl.common.request.Request;

public class SerializingUtilities {
	
	private int expectedReadBytes;
	private int expectedWriteBytes;
	
	public SerializingUtilities() {
		this.expectedReadBytes = -1;
		this.expectedWriteBytes = -1;
	}
	
	public static SerializingUtilities create() {
		return new SerializingUtilities();
	}
	
	public byte[] objectToByteArray(Object o) {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ObjectOutputStream obout;
		try {
			obout = new ObjectOutputStream(bout);
			obout.writeObject(o);
			obout.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bout.toByteArray();
	}
	
	public Object byteArrayToObject(byte[] bytes) {
		ByteArrayInputStream is = new ByteArrayInputStream(bytes);
		try {
			ObjectInputStream ois = new ObjectInputStream(is);
			return ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ByteBuffer packRequest(Request req) {
		byte[] data = objectToByteArray(req);
		int len = data.length;
		byte[] lenData = ByteBuffer.allocate(4).putInt(len).array();
		ByteArrayOutputStream arroutbuf = new ByteArrayOutputStream();
		try {
			arroutbuf.write(lenData);
			arroutbuf.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ByteBuffer buf = ByteBuffer.wrap(arroutbuf.toByteArray());
		expectedWriteBytes = buf.array().length;
		return buf;
	}
	
	public int unpackLength(ByteBuffer inbuf) {
		byte[] lengthField = Arrays.copyOfRange(inbuf.array(), 0, 4);
		return ByteBuffer.wrap(lengthField).getInt();
	}
	
	public boolean allBytesRead(int expectedBytes, int readBytes) {
		return expectedBytes + 4 == readBytes;
	}
	
	public boolean allBytesWritten(int expectedWriteBytes, int writtenBytes) {
		return expectedWriteBytes == writtenBytes;
	}
	
	public Request unpackRequest(ByteBuffer inbuf) {
		return (Request)byteArrayToObject(Arrays.copyOfRange(inbuf.array(), 4, expectedReadBytes + 4));
	}
	
	public int forceRead(ByteBuffer inbuf, AsynchronousSocketChannel sc) {
		Future<Integer> f = sc.read(inbuf);
		try {
			return f.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public int forceWrite(ByteBuffer outbuf, AsynchronousSocketChannel sc) {
		Future<Integer> f = sc.write(outbuf);
		try {
			return f.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public ByteBuffer forceFurtherReadIfNeeded(ByteBuffer inbuf, int readBytes, AsynchronousSocketChannel sc) {
		expectedReadBytes = unpackLength(inbuf);
		while (!allBytesRead(expectedReadBytes, readBytes)) {
			int forcedBytesRead = forceRead(inbuf, sc);
			readBytes += forcedBytesRead;
		}
		// exp == readBytes --> @unpackRequest we know for sure we have all bytes (== exp bytes) ready
		inbuf.flip();
		return inbuf;
	}
	
	public void forceFurtherWriteIfNeeded(ByteBuffer outbuf, int writtenBytes, AsynchronousSocketChannel sc) {
		while (!allBytesWritten(expectedWriteBytes, writtenBytes)) {
			int forcedWrittenBytes = forceWrite(outbuf, sc);
			writtenBytes += forcedWrittenBytes;
		}
	}

}
