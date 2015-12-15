package org.asl.mva;

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
		// number of clients
		// setup devices
		//FixedCapacityDevice disk_B = new FixedCapacityDevice(0, 0.26, 6);
		//LoadDependentDevice fec = new LoadDependentDevice(0, 1, 3, new double[]{0.32, 0.39, 0.42});
		//DelayCenterDevice client = new DelayCenterDevice(1/47.5059, 1, N);
		//LoadDependentDevice tp = new LoadDependentDevice(1, N, new double[]{206.6073, 206.6073, 206.6073, 206.6073, 206.6073});
		//LoadDependentDevice db = new LoadDependentDevice(1, N, new double[]{460.8295, 460.8295, 460.8295, 460.8295, 460.8295});
		LoadDependentDevice db = new LoadDependentDevice(1, N, new double[]{N*417.232022165388, N*414.97036957581, N*412.179989871481, N*408.955559006631,
				N*405.389909679417, N*401.572453203383, N*397.588040395285, N*393.516232876117, N*389.430940509012, N*385.400372764355});
		LoadDependentDevice mw = new LoadDependentDevice(1, N, new double[]{N*8409.87830457775, N*8225.07137306733, N*8062.26893987844, N*7918.38966825234,
				N*7790.83027659137, N*7677.36448083654, N*7576.06552702905, N*7485.2461918263, N*7403.41191580775, N*7329.22396112658});
		DelayCenterDevice client = new DelayCenterDevice(0.0035, 1, N);
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
	
}
