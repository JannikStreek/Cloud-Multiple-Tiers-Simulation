package de.hpi_web.cloudSim.staticTier;

import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Vm;

public class VmFactory {
	public static Vm createVm(int brokerId) {
		return defaultVm(brokerId);
	}
	
	private static Vm defaultVm(int brokerId) {
		int vmid = 0;
		int mips = 1000;
		long size = 10000; // image size (MB)
		int ram = 512; // vm memory (MB)
		long bw = 1000;
		int pesNumber = 1; // number of cpus
		String vmm = "Xen"; // VMM name
		
		return new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
	}
}
