package de.hpi_web.cloudSim.profiling.datacenter;

import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;

import de.hpi_web.cloudSim.multitier.staticTier.DatacenterFactory;

public class DatacenterBuilder extends DatacenterFactory {

	public static Datacenter createDatacenter(String name, DatacenterCharacteristics chars, List<Host> hostList) {

		LinkedList<Storage> storageList = new LinkedList<Storage>(); // we are not adding SAN
		Datacenter datacenter = null;
		try {
			datacenter = new FixedDatacenter(name, chars, new VmAllocationPolicySimple(hostList), storageList, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return datacenter;
	}
}
