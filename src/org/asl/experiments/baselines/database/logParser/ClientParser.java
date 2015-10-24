package org.asl.experiments.baselines.database.logParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
		dir = new File("C:\\Users\\Sandro\\Documents\\ASL_LOGS\\level0");
		File[] logFiles = dir.listFiles();
		tpFile = new File("C:\\Users\\Sandro\\Documents\\ASL_LOGS\\summaries\\level0" + "\\tp_summary.log");
		latFile = new File("C:\\Users\\Sandro\\Documents\\ASL_LOGS\\summaries\\level0" + "\\lat_summary.log");
		idxFile = new File("C:\\Users\\Sandro\\Documents\\ASL_LOGS\\summaries\\level0" + "\\idx_summary.log");
		int maxDBConnections = 30; 
		int startIndex = 2;
		int numClientFiles = maxDBConnections*(maxDBConnections + 1)/2;
		int fileIndex = 0;
		int currDBConnections = 1;
		try {
			tpWriter = new BufferedWriter(new FileWriter(tpFile));
			latWriter = new BufferedWriter(new FileWriter(latFile));
			idxWriter = new BufferedWriter(new FileWriter(idxFile));
			while (fileIndex < numClientFiles) {
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
		 			int line = 0;
		 			while ((s = reader.readLine()) != null) {
		 				if (line == 0) {
		 					// first line is still warmup phase
		 					line++;
		 					continue;
		 				}
		 				String[] parts = s.split(" ");
		 				tpWriter.write(parts[1]);
		 				tpWriter.newLine();
		 				latWriter.write(parts[2]);
		 				latWriter.newLine();
		 				idxWriter.write(String.valueOf(currDBConnections));
		 				idxWriter.newLine();
		 			}
		 			fileIndex++;
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
