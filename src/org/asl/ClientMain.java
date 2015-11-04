package org.asl;

import java.io.IOException;

import org.asl.client.VirtualClient;
import org.asl.common.propertyparser.PropertyKey;
import org.asl.common.propertyparser.PropertyParser;

public class ClientMain {

	private static String mwIp;
	private static int mwPort;
	private static PropertyParser propParser;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		int contentLength = Integer.parseInt(args[0]);
		mwIp = args[1];
		propParser = PropertyParser.create("config/config_common.xml").parse();
		//mwIp = propParser.getProperty(PropertyKey.MIDDLEWARE_IP);
		mwPort = Integer.parseInt(propParser.getProperty(PropertyKey.MIDDLEWARE_PORT));

		Thread t = new Thread(new VirtualClient(mwPort, mwIp, contentLength));
		t.start();
		Thread.sleep(1000*600);
	}
}
