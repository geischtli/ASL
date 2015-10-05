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
	
	private static int expectedBytes;
		
	public static byte[] objectToByteArray(Object o) {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ObjectOutputStream obout;
		try {
			obout = new ObjectOutputStream(bout);
			obout.writeObject(o);
			obout.flush();
			//System.out.println(bout.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bout.toByteArray();
	}
	
	public static Object byteArrayToObject(byte[] bytes) {
		ByteArrayInputStream is = new ByteArrayInputStream(bytes);
		//System.out.println(is.toString());
		try {
			ObjectInputStream ois = new ObjectInputStream(is);
			return ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ByteBuffer packRequest(Request req) {
		byte[] data = SerializingUtilities.objectToByteArray(req);
		int len = data.length;
		byte[] lenData = ByteBuffer.allocate(4).putInt(len).array();
		ByteArrayOutputStream arroutbuf = new ByteArrayOutputStream();
		try {
			arroutbuf.write(lenData);
			arroutbuf.write(data);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return ByteBuffer.wrap(arroutbuf.toByteArray());
	}
	
	public static void unpackLength(ByteBuffer inbuf) {
		byte[] lengthField = Arrays.copyOfRange(inbuf.array(), 0, 4);
		expectedBytes = ByteBuffer.wrap(lengthField).getInt();
	}
	
	public static boolean allBytesRead(int readBytes) {
		return expectedBytes + 4 == readBytes;
	}
	
	public static Request unpackRequest(ByteBuffer inbuf, int readBytes) {
		return (Request)byteArrayToObject(Arrays.copyOfRange(inbuf.array(), 4, readBytes));
	}
	
	public static int forceRead(ByteBuffer inbuf, AsynchronousSocketChannel sc) {
		Future<Integer> f = sc.read(inbuf);
		try {
			return f.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return -1;
	}

}
