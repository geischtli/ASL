//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

package org.asl.common.socket;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;

public class SocketHelper {

	public static AsynchronousSocketChannel openSocket() {
		try {
			return AsynchronousSocketChannel.open();
		} catch (Exception e) {
			System.out.println("Fail occured in open socket");
			e.printStackTrace();
		}
		return null;
	}
	
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
