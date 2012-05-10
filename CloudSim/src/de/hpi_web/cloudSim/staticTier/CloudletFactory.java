package de.hpi_web.cloudSim.staticTier;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;

public class CloudletFactory {
	public static Cloudlet createCloudlet(int brokerId, int vmId) {
		
		return createDefaultCloudlet(brokerId, vmId);
	}
	
	public static Cloudlet createDefaultCloudlet(int brokerId, int vmId) {
		// Cloudlet properties
		int id = 0;
		long length = 250400000;
		long fileSize = 300;
		long outputSize = 300;
		UtilizationModel utilizationModel = new UtilizationModelFull();

		Cloudlet cloudlet = new Cloudlet(id, length, /*pesNumber*/1, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
		cloudlet.setUserId(brokerId);
		cloudlet.setVmId(vmId);
		return cloudlet;
	}
}
