package org.asl.mva;

public class FixedCapacityDevice extends Device {
	
	public FixedCapacityDevice(double S, double V) {
		super(S, V);
	}
	
	@Override
	public double responseTime() {
		R = S*(1.0 + Q);
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