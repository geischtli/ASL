package org.asl.mva;

public class LoadDependentDevice extends Device {

	private double[] P;
	private int n;
	private double[] mu;
	
	public LoadDependentDevice(double V, int N, double[] mu) {
		super(0, V, N);
		// Allocate N+1, because the last iteration wants to write the prob
		// for client N+1
		this.P = new double[N+1];
		this.P[0] = 1.0;
		this.n = 1;
		this.mu = mu;
	}

	@Override
	public double responseTime() {
		double R = 0.0;
		for (int i = 1; i <= n; ++i) {
			R += P[i-1]*i/mu[i-1];
		}
		return R;
	}

	@Override
	public void updateProbabilities(double X) {
		double p_sum = 0.0;
		for (int i = n; i > 0; --i) {
			P[i] = X*V*P[i-1]/mu[i-1];
			p_sum += P[i];
		}
		P[0] = 1 - p_sum;
		// the algorithm iteration is done, next
		// time we have 1 more client
		++n;
	}
	
	@Override
	public void computeUtilization(double X) {
		U = 1.0 - P[0];
	}

}
