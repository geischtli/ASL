package org.asl.common.socket;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;

public class SocketHelper {

	public static void closeSocket(AsynchronousSocketChannel sc) {
		try {
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeSocketAfterException(SocketLocation loc, SocketOperation op, Throwable se, AsynchronousSocketChannel sc) {
		defaultPrint(loc, op, se);
		if (sc.isOpen()) {
			try {
				System.out.println("Forced channel close after exception");
				sc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void closeSocketAfterException(SocketLocation loc, SocketOperation op, Throwable se) {
		defaultPrint(loc, op, se);
	}
	
	public static void defaultPrint(SocketLocation loc, SocketOperation op, Throwable se) {
		System.out.println("In " + loc + ": " + op + " failed");
		se.printStackTrace();
	}
	
}
