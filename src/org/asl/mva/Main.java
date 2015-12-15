package org.asl.mva;

import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		// Input Parameters
		int M = 2;
		int N = 3;
		// Result Vectors
		double[] Ri = new double[M];
		// system response time
		double R = 0.0;
		// sleep time
		double Z = 0.0;
		// system throughput
		double X = 0.0;
		// setup devices
		FixedCapacityDevice disk_B = new FixedCapacityDevice(0, 0.26, 6);
		LoadDependentDevice fec = new LoadDependentDevice(0, 1, 3, new double[]{0.32, 0.39, 0.42});
		// and store them in list
		ArrayList<Device> devices = new ArrayList<Device>();
		devices.add(disk_B);
		devices.add(fec);		
		
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
		for (int m = 0; m < M; ++m) {
			devices.get(m).printInfo(m+1);
		}
	}
	
}
