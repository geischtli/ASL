//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

package org.asl.middleware.database.logParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

public class DatabaseLogParser {

	private static File modFile;
	private static BufferedReader reader;
	private static BufferedWriter writer;
	private static File dir;
	
	public static void main(String[] args) {
		dir = new File("D:\\PostgreSQL\\9.4.4\\data\\pg_log\\");
		File[] logFiles = dir.listFiles();
		modFile = new File(dir.toString() + "\\summary.log");
 		Stack<Double> parses = new Stack<Double>();
 		Stack<Double> binds = new Stack<Double>();
 		Stack<Double> execs = new Stack<Double>();
 		Stack<Double> fullExecutionTimes = new Stack<Double>();
 		int sendCounter = 0;
		try {
			for (File file : logFiles) {
				System.out.print("Start reading file " + file.getName() + "... ");
				reader = new BufferedReader(new FileReader(file));
				String s;
				int line = 0;
				while ((s = reader.readLine()) != null) {
					line++;
					if (s.equals("") || s.contains("CREATE")) {
						continue;
					}
					String[] parts = s.split(" ");
					if (parts[0].substring(0, 3).equals("LOG")) {
						if (s.contains("send_message")) {
							if (s.contains("parse")) {
								parses.push(Double.parseDouble(parts[3]));
							} else if (s.contains("bind")) {
								binds.push(Double.parseDouble(parts[3]));
							} else if (s.contains("execute")) {
								execs.push(Double.parseDouble(parts[3]));
								sendCounter++;
							}
						} else if (s.contains("COMMIT") && s.contains("execute") && sendCounter > 0) {
							double commit = Double.parseDouble(parts[3]);
							double parse = parses.pop();
							double bind = binds.pop();
							if (execs.isEmpty()) {
								System.out.println("uhoh");
							}
							double exec = execs.pop();
							double time = parse + bind + exec + commit;
							fullExecutionTimes.push(time);
							sendCounter--;
						}
					}
				}
				reader.close();
				System.out.println("done");
			}
			System.out.print("Start to write summary ... ");
			writer = new BufferedWriter(new FileWriter(modFile));
			for (int i = 0; i < fullExecutionTimes.size(); i++) {
				writer.write(String.valueOf(fullExecutionTimes.pop()));
				writer.newLine();
			}
			System.out.println("done");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
