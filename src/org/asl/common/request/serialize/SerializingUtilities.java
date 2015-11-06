//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

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
	
	public static SerializingUtilities create() {
		return new SerializingUtilities();
	}
	
	public static byte[] objectToByteArray(Object o) {
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
	
	public static Object byteArrayToObject(byte[] bytes) {
		ByteArrayInputStream is = new ByteArrayInputStream(bytes);
		try {
			ObjectInputStream ois = new ObjectInputStream(is);
			return ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ByteBufferWrapper packRequest(Request req) {
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
		return ByteBufferWrapper.create(buf, buf.array().length);
	}
	
	public static int unpackLength(ByteBuffer inbuf) {
		byte[] lengthField = Arrays.copyOfRange(inbuf.array(), 0, 4);
		return ByteBuffer.wrap(lengthField).getInt();
	}
	
	public static boolean allBytesRead(int expectedReadBytes, int readBytes) {
		return expectedReadBytes + 4 == readBytes;
	}
	
	public static boolean allBytesWritten(int expectedWriteBytes, int writtenBytes) {
		return expectedWriteBytes == writtenBytes;
	}
	
	public static Request unpackRequest(ByteBuffer inbuf, int readBytes) {
		return (Request)byteArrayToObject(Arrays.copyOfRange(inbuf.array(), 4, readBytes));
	}
	
	public static int forceRead(ByteBuffer inbuf, AsynchronousSocketChannel sc) {
		Future<Integer> f = sc.read(inbuf);
		try {
			//System.out.println("im forced to read again...");
			return f.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			//System.out.println("Catched a " + e.getCause());
		}
		System.err.println("forceRead returned -1");
		return -1;
	}
	
	public static int forceWrite(ByteBuffer outbuf, AsynchronousSocketChannel sc) {
		Future<Integer> f = sc.write(outbuf);
		try {
			//System.out.println("before f.get");
			return f.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static ByteBufferWrapper forceFurtherReadIfNeeded(ByteBuffer inbuf, int readBytes, AsynchronousSocketChannel sc) {
		int expectedReadBytes = unpackLength(inbuf);
		while (!allBytesRead(expectedReadBytes, readBytes)) {
			int forcedBytesRead = forceRead(inbuf, sc);
			if (forcedBytesRead == -1) {
				return null;
			}
			readBytes += forcedBytesRead;
		}
		inbuf.flip();
		return ByteBufferWrapper.create(inbuf, readBytes);
	}
	
	public static void forceFurtherWriteIfNeeded(ByteBuffer outbuf, int writtenBytes, int expectedWriteBytes, AsynchronousSocketChannel sc) {
		while (!allBytesWritten(expectedWriteBytes, writtenBytes)) {
			int forcedWrittenBytes = forceWrite(outbuf, sc);
			writtenBytes += forcedWrittenBytes;
		}
	}

}
