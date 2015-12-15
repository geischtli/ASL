package org.asl.mva;

public abstract class Device {
	
	protected double Z;
	protected double S;
	protected double V;
	protected double Q;
	protected double R;
	
	public Device(double Z, double S, double V) {
		this.Z = Z;
		this.S = S;
		this.V = V;
		this.Q = 0.0;
		this.R = 0.0;
	}
	
	public abstract double responseTime();
	public abstract void updateProbabilities(double X);
	
}
