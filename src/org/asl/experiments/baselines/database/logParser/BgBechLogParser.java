package org.asl.experiments.baselines.database.logParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BgBechLogParser {

	private static File modFile;
	private static BufferedReader reader;
	private static BufferedWriter writer;
	private static File dir;
	private static String filename;
	private static List<Integer> maxTPSPerConnectionInc = new ArrayList<Integer>();
	private static List<Integer> maxTPSPerConnectionExc = new ArrayList<Integer>();
	private static BufferedWriter queryWriter;
	private static File queryFile;
	
	public static void main(String[] args) {
		dir = new File("C:\\Users\\Sandro\\Documents\\ASL_LOGS\\level2NIND\\logs");
		filename = "level2_30_30";
		File logFile = new File(dir + "\\" + filename + ".log");
		modFile = new File(dir + "\\" + filename + "_summary.log");
		queryFile = new File(dir + "\\" + filename + "_queries.log");
		try {
			reader = new BufferedReader(new FileReader(logFile));
			writer = new BufferedWriter(new FileWriter(modFile));
			queryWriter = new BufferedWriter(new FileWriter(queryFile));
			String s;
			int clients = 0;
			int threads = 0;
			double latency = 0;
			int tpsInc = 0;
			int tpsExc = 0;
			double sendMessage = 0.0;
			double getQueues = 0.0;
			double readAll = 0.0;
			double readFrom = 0.0;
			double removeTop = 0.0;
			while ((s = reader.readLine()) != null) {
				String[] parts = s.split(" ");
				if (s.contains("clients")) {
					clients = Integer.parseInt(parts[3]);
				} else if (s.contains("threads")) {
					threads = Integer.parseInt(parts[3]);
				} else if (s.contains("latency")) {
					latency = Double.parseDouble(parts[2]); 
				} else if (s.contains("including")) {
					tpsInc = (int) Math.floor(Double.parseDouble(parts[2]));
				} else if (s.contains("excluding")) {
					tpsExc = (int) Math.floor(Double.parseDouble(parts[2]));
					maxTPSPerConnectionInc.add(tpsInc);
					maxTPSPerConnectionExc.add(tpsExc);
					writer.write(clients + " " + tpsInc + " " + tpsExc + " " + latency);
					writer.newLine();
				} else if (s.contains("send message")) {
					sendMessage = Double.parseDouble(parts[0]);
				} else if (s.contains("get_queues")) {
					getQueues = Double.parseDouble(parts[0]);
				} else if (s.contains("read_all")) {
					readAll = Double.parseDouble(parts[0]);
				} else if (s.contains("read_message")) {
					readFrom = Double.parseDouble(parts[0]);
				} else if (s.contains("remove_top")) {
					removeTop = Double.parseDouble(parts[0]);
					// TODO: CONTINUE HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				}
			}
			writer.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		System.out.println("done");
	}
	
}
