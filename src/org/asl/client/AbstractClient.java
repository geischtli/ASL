package org.asl.client;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.asl.common.propertyparser.PropertyKey;
import org.asl.common.propertyparser.PropertyParser;
import org.asl.common.request.Request.RequestType;

public abstract class AbstractClient implements Runnable {

	public static int port;
	public static String ip;
	protected AsynchronousSocketChannel sc;
	protected  List<RequestType> requestList;
	protected Semaphore lock;
	protected PropertyParser propParser;
	public static int INITIAL_BUFSIZE;
	protected ClientInfo ci;
	public static int DURATION_SEC = 1800;
	
	public AbstractClient() {
		AbstractClient.port = -1;
		AbstractClient.ip = "";
		this.sc = null;
		this.requestList = null;
		this.lock = null;
		this.propParser = null;
		AbstractClient.INITIAL_BUFSIZE = -1;
		this.ci = null;
	}
	
	public AbstractClient(int port, String ip) throws IOException {
		AbstractClient.port = port;
		AbstractClient.ip = ip;
		this.requestList = new ArrayList<RequestType>();
		this.lock = new Semaphore(1, true);
		this.propParser = PropertyParser.create("config/config_common.xml").parse();
		AbstractClient.INITIAL_BUFSIZE = Integer.valueOf(propParser.getProperty(PropertyKey.INITIAL_BUFSIZE));
		this.ci = ClientInfo.create();
	}
	
}
