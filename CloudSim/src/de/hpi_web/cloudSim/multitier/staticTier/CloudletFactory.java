package de.hpi_web.cloudSim.multitier.staticTier;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;

import de.hpi_web.cloudSim.multitier.MultiTierCloudlet;

public class CloudletFactory {
	public static final int DEFAULT_PES = 1;
	public static final int DEFAULT_LENGTH = 250400000;
	public static final int DEFAULT_FILESIZE = 300;
	public static final int DEFAULT_OUTPUTSIZE = 300;
	public static Cloudlet createCloudlet(int brokerId) {
		
		return createDefaultCloudlet(0, brokerId);
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
	public static List<MultiTierCloudlet> createCloudlets(int startId, int count, int brokerId) {
		List<MultiTierCloudlet> cloudlets = new ArrayList<MultiTierCloudlet>();
		for (int i = startId; i < startId + count; i++) {
			cloudlets.add(createDefaultCloudlet(i, brokerId));
		}
		return cloudlets;
	}
	public static MultiTierCloudlet createCloudlet(int brokerId, int vmId) {
		
		return createDefaultCloudlet(0, brokerId, vmId);
	}
	private static MultiTierCloudlet createDefaultCloudlet(int cloudletId, int brokerId, int vmId) {
		UtilizationModel utilizationModel = new UtilizationModelFull();

		MultiTierCloudlet cloudlet = new MultiTierCloudlet(cloudletId, DEFAULT_LENGTH, /*pesNumber*/DEFAULT_PES, DEFAULT_FILESIZE, DEFAULT_OUTPUTSIZE, utilizationModel, utilizationModel, utilizationModel);
		cloudlet.setUserId(brokerId);
		cloudlet.setVmId(vmId);
		return cloudlet;
	}
	private static MultiTierCloudlet createDefaultCloudlet(int cloudletId, int brokerId) {
		UtilizationModel utilizationModel = new UtilizationModelFull();

		MultiTierCloudlet cloudlet = new MultiTierCloudlet(cloudletId, DEFAULT_LENGTH, /*pesNumber*/DEFAULT_PES, DEFAULT_FILESIZE, DEFAULT_OUTPUTSIZE, utilizationModel, utilizationModel, utilizationModel);
		cloudlet.setUserId(brokerId);
		return cloudlet;
	}
}
