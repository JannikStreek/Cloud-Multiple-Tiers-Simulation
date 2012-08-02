package de.hpi_web.cloudSim.profiling.datacenter;

import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.Vm;

/*
 * This class extends the CloudSim Vm by some more attributes / properties.
 */
public class ProfilingVm extends Vm {
	
	private double diskAccessRate;

	public ProfilingVm(int id, int userId, double mips, int numberOfPes,
			int ram, long bw, long size, double diskAccessRate, String vmm,
			CloudletScheduler cloudletScheduler) {
		super(id, userId, mips, numberOfPes, ram, bw, size, vmm, cloudletScheduler);
		// TODO Auto-generated constructor stub
		this.diskAccessRate = diskAccessRate;
	}

	public double getDiskAccessRate() {
		return diskAccessRate;
	}

	public void setDiskAccessRate(double diskAccessRate) {
		this.diskAccessRate = diskAccessRate;
	}

	
	

}
