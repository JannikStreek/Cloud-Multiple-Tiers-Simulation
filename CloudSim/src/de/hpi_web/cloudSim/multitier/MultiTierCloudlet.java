package de.hpi_web.cloudSim.multitier;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;
import java.util.Observable;

public class MultiTierCloudlet extends Cloudlet {
	
	private MultiTierCloudletObservable data;
	private int tier;

	public MultiTierCloudlet(int cloudletId, long cloudletLength,
			int pesNumber, long cloudletFileSize, long cloudletOutputSize,
			UtilizationModel utilizationModelCpu,
			UtilizationModel utilizationModelRam,
			UtilizationModel utilizationModelBw) {
		
		super(cloudletId, cloudletLength, pesNumber, cloudletFileSize,
				cloudletOutputSize, utilizationModelCpu, utilizationModelRam,
				utilizationModelBw);
		
		this.data = new MultiTierCloudletObservable();
		data.setParent(this);
	}
	
	public int getTier() {
		return tier;
	}

	public void setTier(int tier) {
		this.tier = tier;
	}
	
	@Override
	public void setCloudletStatus(int newStatus) throws Exception {
		data.setStatus(newStatus);
		super.setCloudletStatus(newStatus);
	}
	
	private class MultiTierCloudletObservable extends Observable{
		int status = 0;
		Cloudlet parent = null;
		
		private void setStatus(int newStatus) {
			status = newStatus;
			hasChanged();
		}
		
		private void setParent(Cloudlet cloudlet) {
			parent = cloudlet;
		}
		
		public Cloudlet getParent(){
			return this.parent;
		}
		
		
	}

}
