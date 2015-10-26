package org.asl.experiments.baselines.database.artificialData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class AritificialMessageDataGenerator {

	private static BufferedWriter writer;
	private static File dataFile;
	private static char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
	private static Random random = new Random();
	
	public static void main(String[] args) {
		String numEntries = args[0];
		String contentLength = args[1];
		dataFile = new File("/home/ec2-user/ASL/db_baseline/initialMessageLoad/messageData_"
				+ numEntries
				+ "_"
				+ contentLength
				+ ".dat");
		try {
			writer = new BufferedWriter(new FileWriter(dataFile));
			int numRows = Integer.parseInt(numEntries);
			int numClients = 100;
			int numQueues = 100;
			int contentLen = Integer.parseInt(contentLength);
			for (int i = 1; i <= numRows; i++) {
				writer.write(String.valueOf(i) + "\t"
						+ String.valueOf(getRandomIntegerInclusiveMax(numClients, random)) + "\t"
						+ String.valueOf(getRandomIntegerInclusiveMax(numClients, random)) + "\t"
						+ String.valueOf(getRandomIntegerInclusiveMax(numQueues, random)) + "\t"
						+ getRandomStringOfLength(contentLen) + "\t"
						+ getCurrentDateTime());
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("done");
	}
	
	public static String getCurrentDateTime() {
		Date dNow = new Date( );
		SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss.SSS");
		return ft.format(dNow);
	}
	
	public static int getRandomIntegerInclusiveMax(int numClients, Random random) {
		return random.nextInt(numClients) + 1;
	}
	
	public static String getRandomStringOfLength(int len) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
		    char c = chars[random.nextInt(chars.length)];
		    sb.append(c);
		}
		return sb.toString();
	}
	
}
