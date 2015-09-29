package org.asl.common.message.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.asl.common.message.Message;

public class SerializingUtilities {
	
	public static byte[] objectToByteArray(Object o) {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ObjectOutputStream obout;
		try {
			obout = new ObjectOutputStream(bout);
			obout.writeObject(o);
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
}
