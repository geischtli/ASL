package org.asl.experiments.baselines.client.loadgenerator;

import java.io.IOException;

import org.asl.common.propertyparser.PropertyKey;
import org.asl.common.propertyparser.PropertyParser;

public class BenchClientMain {

	private static String mwIp;
	private static int mwPort;
	private static PropertyParser propParser;
	
	public static void main(String[] args) {
		propParser = PropertyParser.create("config/config_common.xml").parse();
		mwIp = propParser.getProperty(PropertyKey.MIDDLEWARE_IP);
		mwPort = Integer.parseInt(propParser.getProperty(PropertyKey.MIDDLEWARE_PORT));
		
		int numClients = 100;
		for (int i = 0; i < numClients; i++) {
			try {
				Thread t = new Thread(new BenchClient(mwPort, mwIp));
				t.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			Thread.sleep(1000*600);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
}
