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
	public static void main(String[] args) {
		dir = new File("C:\\Users\\Sandro\\Documents\\eclipse\\ASL\\db_baseline\\logs\\");
		filename = "level2_30_20";
		File logFile = new File(dir + "\\" + filename + ".log");
		modFile = new File(dir + "\\" + filename + "_plotReady.log");
		try {
			reader = new BufferedReader(new FileReader(logFile));
			writer = new BufferedWriter(new FileWriter(modFile));
			String s;
			int clients = 0;
			int threads = 0;
			double latency = 0;
			int tpsInc = 0;
			int tpsExc = 0;
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
