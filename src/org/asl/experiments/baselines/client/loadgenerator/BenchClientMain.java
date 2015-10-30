package org.asl.experiments.baselines.client.loadgenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.asl.common.propertyparser.PropertyKey;
import org.asl.common.propertyparser.PropertyParser;

public class BenchClientMain {

	private static String mwIp;
	private static int mwPort;
	private static PropertyParser propParser;
	public static BufferedWriter logWriter;
	
	public static void main(String[] args) {
		propParser = PropertyParser.create("config/config_common.xml").parse();
		mwIp = propParser.getProperty(PropertyKey.MIDDLEWARE_IP);
		mwPort = Integer.parseInt(propParser.getProperty(PropertyKey.MIDDLEWARE_PORT));
		
		try {
			logWriter = new BufferedWriter(new FileWriter("/home/ec2-user/ASL/client_baseline/clientTimes.log", false));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		int numClients = Integer.parseInt(args[0]);
		System.out.println("Will run " + numClients + " clients");
		
		for (int i = 0; i < numClients; i++) {
			try {
				Thread t = new Thread(new BenchClient(mwPort, mwIp, logWriter));
				t.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			BenchClientMain.logWriter.close();
			System.out.println("log writer closed");
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
}
