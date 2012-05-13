package de.hpi_web.cloudSim.multitier;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.core.SimEntity;

import de.hpi_web.cloudSim.multitier.datacenter.CloudController;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class MultiTierCloudlet extends Cloudlet {
	private List<CloudController> listeners = new ArrayList<CloudController>();
	private int tier;
	private MultiTierCloudlet parent;

	private SimEntity originator;
	
	public static int TIER_DB = 2;
	public static int TIER_APP = 1;
	public static int TIER_SERVER = 0;

	public MultiTierCloudlet(int cloudletId, long cloudletLength,
			int pesNumber, long cloudletFileSize, long cloudletOutputSize,
			UtilizationModel utilizationModelCpu,
			UtilizationModel utilizationModelRam,
			UtilizationModel utilizationModelBw) {
		
		super(cloudletId, cloudletLength, pesNumber, cloudletFileSize,
				cloudletOutputSize, utilizationModelCpu, utilizationModelRam,
				utilizationModelBw);
	}
	
	public int getTier() {
		return tier;
	}

	public void setTier(int tier) {
		this.tier = tier;
	}
	
	@Override
	public void setCloudletStatus(int newStatus) throws Exception {
		notifyListeners();
		super.setCloudletStatus(newStatus);
	}
		
	private void notifyListeners() {
		for (CloudController listener : listeners) {
			listener.propertyChange(this);
		}
	}

	public void addChangeListener(CloudController newListener) {
		listeners.add(newListener);
	}
	
	public MultiTierCloudlet getParent() {
		return parent;
	}

	public SimEntity getOriginator() {
		return originator;
	}

}
