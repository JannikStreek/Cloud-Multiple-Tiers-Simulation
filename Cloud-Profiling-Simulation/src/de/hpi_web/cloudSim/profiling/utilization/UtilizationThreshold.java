package de.hpi_web.cloudSim.profiling.utilization;

public class UtilizationThreshold {
	private int upper;
	private int lower;

	public UtilizationThreshold(int upper, int lower) {
		this.upper = upper;
		this.lower = lower;
	}

	public int getUpper() {
		return upper;
	}

	public void setUpper(int upper) {
		this.upper = upper;
	}
	
	public int getLower() {
		return lower;
	}

	public void setLower(int lower) {
		this.lower = lower;
	}
	
}
