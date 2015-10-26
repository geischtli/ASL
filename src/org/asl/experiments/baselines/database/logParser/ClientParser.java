package org.asl.experiments.baselines.database.logParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientParser {

	private static File tpFile;
	private static File latFile;
	private static File idxFile;
	private static BufferedReader reader;
	private static BufferedWriter tpWriter;
	private static BufferedWriter latWriter;
	private static BufferedWriter idxWriter;
	private static File dir;
	
	public static void main(String[] args) {
		int level = 2;
		String basedir = "C:\\Users\\Sandro\\Documents\\ASL_LOGS\\DB_BASELINE 31-60 DB CONNS\\";
		dir = new File(basedir + "level" + String.valueOf(level) + "WIND");
		File[] logFiles = dir.listFiles();
		tpFile = new File(basedir + "summaries\\level" + String.valueOf(level)  + "WIND" + "\\tp_summary.log");
		latFile = new File(basedir + "summaries\\level" + String.valueOf(level) + "WIND" + "\\lat_summary.log");
		idxFile = new File(basedir + "summaries\\level" + String.valueOf(level) + "WIND" + "\\idx_summary.log");
		int maxDBConnections = 60; 
		int startIndex = 2;
		//int numClientFiles = maxDBConnections*(maxDBConnections + 1)/2;
		int numClientFiles = 406;
		int fileIndex = 0;
		int currDBConnections = 31;
		try {
			tpWriter = new BufferedWriter(new FileWriter(tpFile));
			latWriter = new BufferedWriter(new FileWriter(latFile));
			idxWriter = new BufferedWriter(new FileWriter(idxFile));
			List<Integer> tpSum = null;
			List<Integer> latSum = null;
			int numEffecitveLines = 29;
			while (currDBConnections <= maxDBConnections) {
				String id = "";
		 		for (int i = 1; i <= currDBConnections; i++) {
		 			File file = logFiles[startIndex + fileIndex];
		 			// sanity check
		 			String path = file.getAbsolutePath();
	 				String[] filepathparts = path.split("\\\\");
	 				String filename = filepathparts[filepathparts.length - 1];
	 				String[] filenameparts = filename.split("pgbench_log.");
		 			if (i == 1) {
		 				String currId = filenameparts[filenameparts.length - 1];
		 				id = currId;
		 				tpSum = new ArrayList<Integer>(Collections.nCopies(numEffecitveLines, 0));
		 				latSum = new ArrayList<Integer>(Collections.nCopies(numEffecitveLines, 0));
		 			} else {
		 				String currIdTemp = filenameparts[filenameparts.length - 1];
		 				String[] currIds = currIdTemp.split("\\.");
		 				String currId = currIds[0];
		 				if (!id.equals(currId)) {
		 					System.out.println("NOT THE SAME FAMILIY ID");
		 				}
		 			}
		 			reader = new BufferedReader(new FileReader(file));
		 			String s;
		 			// skip first line due to warm-up phase
		 			// second line is at 0 -> useful index
		 			int line = -2;
		 			while ((s = reader.readLine()) != null) {
		 				line++;
		 				if (line == -1) {
		 					// first line is still warmup phase
		 					continue;
		 				}
		 				String[] parts = s.split(" ");
		 				int currTpSum = tpSum.get(line);
		 				tpSum.set(line, currTpSum + Integer.parseInt(parts[1]));
		 				int currLatSum = latSum.get(line);
		 				latSum.set(line, currLatSum + Integer.parseInt(parts[2]));
		 			}
		 			fileIndex++;
		 		}
		 		// write the summed up latencies and throughputs
	 			for (int j = 0; j < numEffecitveLines; j++) {
	 				tpWriter.write(String.valueOf(tpSum.get(j)));
	 				tpWriter.newLine();
	 				latWriter.write(String.valueOf(latSum.get(j)));
	 				latWriter.newLine();
	 				idxWriter.write(String.valueOf(currDBConnections));
	 				idxWriter.newLine();
	 			}
		 		currDBConnections++;
			}
			tpWriter.close();
			latWriter.close();
			idxWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("done");
	}
	
}
