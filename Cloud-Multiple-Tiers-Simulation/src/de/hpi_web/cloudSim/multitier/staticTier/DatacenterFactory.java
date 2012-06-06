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
	
	/* DC Characteristics */
	public static final double  DEFAULT_COST  =       	3.0;		// cost for processing
	public static final double  DEFAULT_COST_MEM  =   	0.05;		// cost for main memory
	public static final double  DEFAULT_COST_STORAGE =	0.001;		// cost for storage
	public static final double  DEFAULT_COST_BW =	    0.0;		// cost for bandwith
	public static final double  DEFAULT_TIMEZONE =     10.0;		// time zone of this resource

	public static final String ARCH = "x86";				// systen architecture
	public static final String OS = "Linux";				// operating system
	public static final String VMM = "Xen";					// virtual machine manager (hypervisor)
	
	/* Host Configuration */
	public static final int  DEFAULT_PES  =       1;		// number of CPUS
	public static final int  DEFAULT_MIPS =    1200;		// MIPS per CPU
	public static final int  DEFAULT_RAM  =    4096;  		// vm memory (MB)
	public static final long DEFAULT_STORAGE = 1600000; 	// image size (MB)
	public static final long DEFAULT_BW   =   10000;		// total bandwith available, 10GBit/s

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

		for (int i = 0; i < DEFAULT_PES; i++)
			peList.add(new Pe(i, new PeProvisionerSimple(DEFAULT_MIPS)));

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
	
	protected static DatacenterCharacteristics defaultCharacteristics(List<Host> hostList) {
		return new DatacenterCharacteristics(
				ARCH, OS, VMM, hostList, DEFAULT_TIMEZONE, DEFAULT_COST, DEFAULT_COST_MEM,
				DEFAULT_COST_STORAGE, DEFAULT_COST_BW);
	}
	
	protected static Host defaultHost(int hostId, List<Pe> peList) {
		return new Host(
				hostId,
				new RamProvisionerSimple(DEFAULT_RAM),
				new BwProvisionerSimple(DEFAULT_BW),
				DEFAULT_STORAGE,
				peList,
				new VmSchedulerTimeShared(peList)
			);
	}
}
