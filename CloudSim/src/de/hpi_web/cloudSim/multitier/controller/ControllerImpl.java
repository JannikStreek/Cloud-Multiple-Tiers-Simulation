package de.hpi_web.cloudSim.multitier.controller;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.core.CloudSim;

import de.hpi_web.cloudSim.multitier.MultiTierCloudlet;

public class ControllerImpl implements CloudController {
	
	@Override
	public void propertyChange(MultiTierCloudlet cloudlet) {
		
		if (cloudlet.getStatus() == Cloudlet.INEXEC) {
			
			int tier = cloudlet.getTier();
	        switch (tier) {
	        case 1:  tier = MultiTierCloudlet.TIER_SERVER;
	                 break;
	        case 2:  tier = MultiTierCloudlet.TIER_APP;
	                 break;
	        case 3:  tier = MultiTierCloudlet.TIER_DB; //last tier, do nothing
	                 break;
	        default: tier = 0;
	                 break;
			
	        }

		}
	}
	
	private void createMultiTierCloudlet(int tier, int src, int dest) {
		
		int id = 0;
        long length = 400000;
        long fileSize = 300;
        long outputSize = 300;
        UtilizationModel utilizationModel = new UtilizationModelFull();

        MultiTierCloudlet cloudlet = new MultiTierCloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
        cloudlet.setTier(tier);
        CloudSim.send(src, dest, delay, tag, cloudlet);
        Host
		
	}

}
