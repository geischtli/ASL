package org.asl.mva;

public abstract class Device {
	
	protected double S;
	protected double V;
	protected double Q;
	protected double R;
	protected int N;
	protected double X;
	protected double U;
	
	public Device(double S, double V, int N) {
		initVars(S, V, N);
	}
	
	public Device(double S, double V) {
		initVars(S, V, 0);
	}
	
	private void initVars(double S, double V, int N) {
		this.S = S;
		this.V = V;
		this.Q = 0.0;
		this.R = 0.0;
		this.N = N;
	}
	
	public double getV() {
		return V;
	}
	
	public void computeThroughput(double X) {
		this.X = X*V;
	}
	
	public void printInfo(int i) {
		System.out.println("Device " + i + ": ");
		System.out.println("\tThroughput: " + X);
		System.out.println("\tUtilization: " + U);
		System.out.println("\tResponse Time: " + getR());
	}
	
	public abstract double responseTime();
	public abstract void updateProbabilities(double X);
	public abstract void computeUtilization(double X);
	public abstract double getR();
}
