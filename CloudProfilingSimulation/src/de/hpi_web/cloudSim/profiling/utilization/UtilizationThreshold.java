package de.hpi_web.cloudSim.profiling.utilization;

public class UtilizationThreshold {
	private int upper;
	private int lower;

	public UtilizationThreshold(int a, int b) {
		this.upper = Math.max(a, b);
		this.lower = Math.min(a, b);
	}
	
	public boolean isValid() {
		return (upper > 0 && lower > 0 && lower < upper);
	}
	
	public boolean belowThreshold(int value) {
		if (!isValid())
			return true;
		return value < lower;
	}

	public boolean belowThreshold(double value) {
		if (!isValid())
			return true;
		return value < lower;
	}

	public boolean aboveThreshold(int value) {
		if (!isValid())
			return false;
		return value > upper;
	}

	public boolean aboveThreshold(double value) {
		if (!isValid())
			return false;
		return value > upper;
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
