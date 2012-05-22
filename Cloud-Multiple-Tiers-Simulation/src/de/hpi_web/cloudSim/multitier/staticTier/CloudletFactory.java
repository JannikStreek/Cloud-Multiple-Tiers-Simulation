package de.hpi_web.cloudSim.multitier.staticTier;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;

import de.hpi_web.cloudSim.multitier.MultiTierCloudlet;
import de.hpi_web.cloudSim.multitier.cloudlet.MultiTierWorkload;
import de.hpi_web.cloudSim.multitier.datacenter.DatacenterAffinityBroker;

public class CloudletFactory {
	public static final int DEFAULT_PES = 1;
	public static final int DEFAULT_LENGTH = 30;//250400000;
	public static final int DEFAULT_FILESIZE = 1;//300;
	public static final int DEFAULT_OUTPUTSIZE = 1;//300;
	
	
	
	public static Cloudlet createCloudlet(DatacenterBroker broker) {
		
		return createDefaultCloudlet(0, broker);
	}
	
	/**
	 * Create a List of default cloudlets
	 * 
	 * @param startId first ID to be used
	 * @param count number of cloudlets to be created
	 * @param brokerId owner (broker ID)
	 * @return List<Cloudlet>
	 * @pre startId >= 0
	 * @pre startId >= 0
	 * @post $none
	 */
	public static List<MultiTierCloudlet> createCloudlets(int startId, int count, DatacenterBroker broker) {
		List<MultiTierCloudlet> cloudlets = new ArrayList<MultiTierCloudlet>();
		for (int i = startId; i < startId + count; i++) {
			cloudlets.add(createDefaultCloudlet(i, broker));
		}
		return cloudlets;
	}
	
	public static MultiTierCloudlet createCloudlet(DatacenterBroker broker, int vmId) {
		
		return createDefaultCloudlet(0, broker, vmId);
	}
	
	private static MultiTierCloudlet createDefaultCloudlet(int cloudletId, DatacenterBroker broker, int vmId) {
		UtilizationModel utilizationModel = new UtilizationModelFull();

		MultiTierCloudlet cloudlet = new MultiTierCloudlet(cloudletId, DEFAULT_LENGTH, /*pesNumber*/DEFAULT_PES, DEFAULT_FILESIZE, DEFAULT_OUTPUTSIZE, utilizationModel, utilizationModel, utilizationModel);
		cloudlet.setUserId(broker.getId());
		cloudlet.setVmId(vmId);
		return cloudlet;
	}
	
	private static MultiTierCloudlet createDefaultCloudlet(int cloudletId, DatacenterBroker broker) {
		UtilizationModel utilizationModel = new UtilizationModelFull();

		MultiTierCloudlet cloudlet = new MultiTierCloudlet(cloudletId, DEFAULT_LENGTH, /*pesNumber*/DEFAULT_PES, DEFAULT_FILESIZE, DEFAULT_OUTPUTSIZE, utilizationModel, utilizationModel, utilizationModel);
		cloudlet.setUserId(broker.getId());
		return cloudlet;
	}
	
	public static List<MultiTierCloudlet> createCloudletsForWorkload(int startId, DatacenterBroker broker, MultiTierWorkload workload, int currentTier) {
		DatacenterAffinityBroker b = (DatacenterAffinityBroker) broker;
		DatacenterAffinityBroker s = b.getSuccessor();
		List<MultiTierCloudlet> cloudlets = createCloudlets(startId, workload.getWorkloadForTier(currentTier), broker);
		
		int id = startId;
		if (s != null)
			for (MultiTierCloudlet c : cloudlets) {
				c.setChildren(createCloudletsForWorkload(id, s, workload, currentTier+1));
				id += workload.getWorkloadForTier(currentTier);
			}
		
		return cloudlets;
	}
}
