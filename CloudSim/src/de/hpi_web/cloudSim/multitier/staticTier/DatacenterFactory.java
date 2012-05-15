package de.hpi_web.cloudSim.multitier.staticTier;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

public class DatacenterFactory {
	
	/**
	 * Creates the datacenter.
	 *
	 * @param name the name
	 *
	 * @return the datacenter
	 */
	public static Datacenter createDatacenter(String name) {
		return createDatacenter(name, 0, 1);
	}
	/**
	 * Creates a new default Datacenter
	 * 
	 * @param startId first ID to be used
	 * @param hosts number of default hosts to be created
	 * @param name Name of this datacenter
	 * @return Datacenter
	 * @pre startId >= 0
	 * @post $none
	 */
	public static Datacenter createDatacenter(String name, int startId, int hosts) {
		List<Host> hostList = new ArrayList<Host>();
		List<Pe> peList = new ArrayList<Pe>();

		int mips = 1200;
		peList.add(new Pe(0, new PeProvisionerSimple(mips)));
		peList.add(new Pe(1, new PeProvisionerSimple(mips)));
		peList.add(new Pe(2, new PeProvisionerSimple(mips)));
		peList.add(new Pe(3, new PeProvisionerSimple(mips)));

		for(int hostId = startId; hostId < startId + hosts; hostId++) {
			hostList.add(defaultHost(hostId, peList));
		}
		return createDatacenter(name, defaultCharacteristics(hostList), hostList);
	}
	public static Datacenter createDatacenter(String name, DatacenterCharacteristics chars, List<Host> hostList) {

		LinkedList<Storage> storageList = new LinkedList<Storage>(); // we are not adding SAN
		Datacenter datacenter = null;
		try {
			datacenter = new Datacenter(name, chars, new VmAllocationPolicySimple(hostList), storageList, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return datacenter;
	}
	
	private static DatacenterCharacteristics defaultCharacteristics(List<Host> hostList) {
		String arch = "x86"; // system architecture
		String os = "Linux"; // operating system
		String vmm = "Xen";
		double time_zone = 10.0; // time zone this resource located
		double cost = 3.0; // the cost of using processing in this resource
		double costPerMem = 0.05; // the cost of using memory in this resource
		double costPerStorage = 0.001; // the cost of using storage in this
										// resource
		double costPerBw = 0.0; // the cost of using bw in this resource

		return new DatacenterCharacteristics(
				arch, os, vmm, hostList, time_zone, cost, costPerMem,
				costPerStorage, costPerBw);
	}
	
	private static Host defaultHost(int hostId, List<Pe> peList) {
		int ram = 2048;
		long storage = 1000000;
		int bw = 10000;
		return new Host(
				hostId,
				new RamProvisionerSimple(ram),
				new BwProvisionerSimple(bw),
				storage,
				peList,
				new VmSchedulerTimeShared(peList)
			);
	}
}
