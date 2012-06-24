package de.hpi_web.cloudSim.profiling.builders;

import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;

import de.hpi_web.cloudSim.multitier.staticTier.DatacenterFactory;
import de.hpi_web.cloudSim.profiling.datacenter.FixedDatacenter;

public class DatacenterBuilder {
	
	/* DC Characteristics */
	public static final double  DEFAULT_COST  =       	3.0;		// cost for processing
	public static final double  DEFAULT_COST_MEM  =   	0.05;		// cost for main memory
	public static final double  DEFAULT_COST_STORAGE =	0.001;		// cost for storage
	public static final double  DEFAULT_COST_BW =	    0.0;		// cost for bandwith
	public static final double  DEFAULT_TIMEZONE =     10.0;		// time zone of this resource

	public static final String ARCH = "x86";				// systen architecture
	public static final String OS = "Linux";				// operating system
	public static final String VMM = "Xen";					// virtual machine manager (hypervisor)

	private String name;
	private DatacenterCharacteristics characteristics;
	private VmAllocationPolicy vmAllocationPolicy;
	private List<Storage> storageList;
	private double lastProcessTime;
	
	DatacenterBuilder(String name) {
		this.name = name;
		this.lastProcessTime = 0;
		this.storageList = new LinkedList<Storage>();
	}
	
	public FixedDatacenter build() throws Exception {
		return new FixedDatacenter(this);
	}
	
	// could interfere with vmAllocation Policy
//	public DatacenterBuilder setCharacteristics(DatacenterCharacteristics c) {
//		this.characteristics = c;
//		return this;
//	}
	
	public DatacenterBuilder setHostList(List<Host> hostList) {
		this.characteristics = new DatacenterCharacteristics(
				ARCH, OS, VMM, hostList, DEFAULT_TIMEZONE, DEFAULT_COST, DEFAULT_COST_MEM,
				DEFAULT_COST_STORAGE, DEFAULT_COST_BW);
		this.vmAllocationPolicy = new VmAllocationPolicySimple(hostList);
		return this;
	}


	public String getName() {
		return name;
	}

	public DatacenterBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public VmAllocationPolicy getVmAllocationPolicy() {
		return vmAllocationPolicy;
	}

	public DatacenterBuilder setVmAllocationPolicy(VmAllocationPolicy vmAllocationPolicy) {
		this.vmAllocationPolicy = vmAllocationPolicy;
		return this;
	}

	public List<Storage> getStorageList() {
		return storageList;
	}

	public DatacenterBuilder setStorageList(List<Storage> storageList) {
		this.storageList = storageList;
		return this;
	}

	public double getLastProcessTime() {
		return lastProcessTime;
	}

	public DatacenterBuilder setLastProcessTime(double lastProcessTime) {
		this.lastProcessTime = lastProcessTime;
		return this;
	}

	public DatacenterCharacteristics getCharacteristics() {
		return characteristics;
	}

}
