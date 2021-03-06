package org.asl.mva;

public class DelayCenterDevice extends Device {
	
	public DelayCenterDevice(double S, double V, int N, String name) {
		super(S, V, N, name);
	}

	@Override
	public double responseTime() {
		R = S;
		return R;
	}

	@Override
	public void updateProbabilities(double X) {
		Q = X*V*R;
	}

	@Override
	public void computeUtilization(double X) {
		U = X*S*V;
	}
	
	@Override
	public double getR() {
		return R;
	}
	
}