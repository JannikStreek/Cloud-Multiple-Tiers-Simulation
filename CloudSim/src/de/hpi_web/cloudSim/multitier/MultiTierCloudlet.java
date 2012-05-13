package de.hpi_web.cloudSim.multitier;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.core.SimEntity;

import de.hpi_web.cloudSim.multitier.datacenter.CloudController;
import de.hpi_web.cloudSim.multitier.datacenter.DatacenterAffinityBroker;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class MultiTierCloudlet extends Cloudlet {
	private List<CloudController> listeners = new ArrayList<CloudController>();
	private int tier;
	private MultiTierCloudlet parent;

	private DatacenterAffinityBroker originator;
	
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
	
	public void setOriginator(DatacenterAffinityBroker originator) {
		this.originator = originator;
	}

	public MultiTierCloudlet(Cloudlet cloudlet) {
		super(cloudlet.getCloudletId(), cloudlet.getCloudletLength(), cloudlet.getNumberOfPes(), cloudlet.getCloudletFileSize(),
				cloudlet.getCloudletOutputSize(), cloudlet.getUtilizationModelCpu(), cloudlet.getUtilizationModelRam(),
				cloudlet.getUtilizationModelBw());
		setVmId(cloudlet.getVmId());
		setUserId(cloudlet.getUserId());
		
	}
	
	public int getTier() {
		return tier;
	}

	public void setTier(int tier) {
		this.tier = tier;
	}
	
	public void setParent(MultiTierCloudlet parent) {
		this.parent = parent;
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

	public DatacenterAffinityBroker getOriginator() {
		return originator;
	}

}
