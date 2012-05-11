package de.hpi_web.cloudSim.staticTier;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Vm;

public class VmFactory {
	public static Vm createVm(int brokerId) {
		return defaultVm(0, brokerId);
	}

	public static List<Vm> createVms(int count, int brokerId) {
		List<Vm> vms = new ArrayList<Vm>();
		for (int i = 0; i < count; i++)
			vms.add(defaultVm(i, brokerId));
		return vms;
	}
	
	private static Vm defaultVm(int vmid, int brokerId) {
		int mips = 1000;
		long size = 10000; // image size (MB)
		int ram = 512; // vm memory (MB)
		long bw = 1000;
		int pesNumber = 1; // number of cpus
		String vmm = "Xen"; // VMM name
		
		return new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
	}
}
