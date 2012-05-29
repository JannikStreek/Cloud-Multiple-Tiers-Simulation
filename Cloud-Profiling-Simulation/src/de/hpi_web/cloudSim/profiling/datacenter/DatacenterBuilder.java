package de.hpi_web.cloudSim.profiling.datacenter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;

import de.hpi_web.cloudSim.multitier.staticTier.DatacenterFactory;

public class DatacenterBuilder extends DatacenterFactory {
	public static FixedDatacenter createDatacenter(String name, int startId, int hosts) {
		List<Host> hostList = new ArrayList<Host>();
		List<Pe> peList = new ArrayList<Pe>();

		for (int i = 0; i < DEFAULT_PES; i++)
			peList.add(new Pe(i, new PeProvisionerSimple(DEFAULT_MIPS)));

		for(int hostId = startId; hostId < startId + hosts; hostId++) {
			hostList.add(defaultHost(hostId, peList));
		}
		return createDatacenter(name, defaultCharacteristics(hostList), hostList);
	}
	
	public static FixedDatacenter createDatacenter(String name, DatacenterCharacteristics chars, List<Host> hostList) {

		LinkedList<Storage> storageList = new LinkedList<Storage>(); // we are not adding SAN
		FixedDatacenter datacenter = null;
		try {
			datacenter = new FixedDatacenter(name, chars, new VmAllocationPolicySimple(hostList), storageList, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return datacenter;
	}
}
