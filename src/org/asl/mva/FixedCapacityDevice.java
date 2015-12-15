package org.asl.mva;

public class FixedCapacityDevice extends Device {
	
	public FixedCapacityDevice(double Z, double S, double V) {
		super(Z, S, V);
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

}
