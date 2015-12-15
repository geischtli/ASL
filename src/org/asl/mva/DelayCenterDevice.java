package org.asl.mva;

public class DelayCenterDevice extends Device {
	
	public DelayCenterDevice(double Z, double S, double V, int N) {
		super(Z, S, V, N);
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
	
}