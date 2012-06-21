package de.hpi_web.cloudSim.profiling.datacenter;

public class UtilWrapper {
	private double cpuUtil;
	private double memUtil;
	private double diskReadUtil;
	private double diskWriteUtil;
	private double bwInUtil;
	private double bwOutUtil;
	
	public UtilWrapper(double cpuUtil, double memUtil, double diskReadUtil,
			double diskWriteUtil, double networkInUtil, double networkOutUtil) {
		this.cpuUtil = cpuUtil;
		this.memUtil = memUtil;
		this.diskReadUtil = diskReadUtil;
		this.diskWriteUtil = diskWriteUtil;
		this.bwInUtil = networkInUtil;
		this.bwOutUtil = networkOutUtil;
	}
	public double getCpuUtil() {
		return cpuUtil;
	}
	public void setCpuUtil(double cpuUtil) {
		this.cpuUtil = cpuUtil;
	}
	public double getMemUtil() {
		return memUtil;
	}
	public void setMemUtil(double memUtil) {
		this.memUtil = memUtil;
	}
	public double getDiskReadUtil() {
		return diskReadUtil;
	}
	public void setDiskReadUtil(double diskReadUtil) {
		this.diskReadUtil = diskReadUtil;
	}
	public double getDiskWriteUtil() {
		return diskWriteUtil;
	}
	public void setDiskWriteUtil(double diskWriteUtil) {
		this.diskWriteUtil = diskWriteUtil;
	}
	public double getBwInUtil() {
		return bwInUtil;
	}
	public void setBwInUtil(double bwInUtil) {
		this.bwInUtil = bwInUtil;
	}
	public double getBwOutUtil() {
		return bwOutUtil;
	}
	public void setBwOutUtil(double bwOutUtil) {
		this.bwOutUtil = bwOutUtil;
	}

}
