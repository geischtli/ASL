package org.asl.mva;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		// Input Parameters
		int N = 10;
		// system response time
		double R = 0.0;
		// sleep time
		double Z = 0.0;
		// system throughput
		double X = 0.0;
		// mu's
		double[] mw_mu = null;
		double[] db_mu = null;
		// read mu's from db and mw from files
		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					"C:\\Users\\Sandro\\Documents\\eclipse\\ASL\\src\\org\\asl\\mva\\mu_mw.txt"));
			mw_mu = readMu(reader);
			reader = new BufferedReader(new FileReader(
					"C:\\Users\\Sandro\\Documents\\eclipse\\ASL\\src\\org\\asl\\mva\\mu_db.txt"));
			db_mu = readMu(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// setup devices
		LoadDependentDevice mw = new LoadDependentDevice(1, N, mw_mu, "Middleware");
		LoadDependentDevice db = new LoadDependentDevice(1, N, db_mu, "Database");
		DelayCenterDevice client = new DelayCenterDevice(0.0014, 1, N, "Client");
		// and store them in list
		ArrayList<Device> devices = new ArrayList<Device>();
		devices.add(mw);
		devices.add(db);
		devices.add(client);
		// number of devices
		int M = devices.size();
		// Result Vectors
		double[] Ri = new double[M];
		
		// run the algorithm
		for (int n = 1; n <= N; ++n) {
			R = 0.0;
			for (int m = 0; m < M; ++m) {
				Ri[m] = devices.get(m).responseTime();
				R += Ri[m]*devices.get(m).getV();
			}
			X = n/(Z + R);
			for (int m = 0; m < M; ++m) {
				devices.get(m).updateProbabilities(X);
			}
		}
		
		// do post computations
		for (int m = 0; m < M; ++m) {
			devices.get(m).computeThroughput(X);
			devices.get(m).computeUtilization(X);
		}
		
		// print some information
		System.out.println("System Throughput: " + X);
		System.out.println("System Response Time: " + R);
		System.out.println();
		for (int m = 0; m < M; ++m) {
			devices.get(m).printInfo(m+1);
		}
	}
	
	private static double[] readMu(BufferedReader reader) throws IOException {
		double[] ret = new double[120];
		String line;
		int i = 0;
		while ((line = reader.readLine()) != null) {
			ret[i++] = Double.parseDouble(line);
		}
		return ret;
	}
	
}
