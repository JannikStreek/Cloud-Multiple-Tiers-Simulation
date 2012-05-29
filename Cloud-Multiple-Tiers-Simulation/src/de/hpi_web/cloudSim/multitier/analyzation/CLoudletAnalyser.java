package de.hpi_web.cloudSim.multitier.analyzation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;

import de.hpi_web.cloudSim.multitier.MultiTierCloudlet;
import de.hpi_web.cloudSim.multitier.datacenter.DatacenterAffinityBroker;

public class CLoudletAnalyser {
	
	private List<DatacenterAffinityBroker> usedBroker;
	private List<Datacenter> datacenterList;
	
	
	public CLoudletAnalyser(List<DatacenterAffinityBroker> brokerList, List<Datacenter> datacenterList) {
		this.usedBroker = brokerList;
		this.datacenterList = datacenterList;
	}
	
//	public void startAnalyzation() {
//		calculateTurnAroundTime();
//	}

	// TODO big effort for nothing...........
//	private void calculateTurnAroundTime() {
//		for(DatacenterAffinityBroker broker : usedBroker) {
//			Map<Integer, List<MultiTierCloudlet>> dcMapping = new HashMap<Integer, List<MultiTierCloudlet>>();
//			List<Integer> datacenterIdList = broker.getDcAffinityList();
//			//init resultSet
//			for(int datacenterId : datacenterIdList) {
//				dcMapping.put(datacenterId, new ArrayList<MultiTierCloudlet>());
//			}
//			List<MultiTierCloudlet> list = broker.getCloudletReceivedList();
//			for(int key : dcMapping.keySet()) {
//				Datacenter datacenter = searchInDcListFor(key);
//				
//				
//				dcMapping.put(key, allCloudletsForDc(datacenter));
//				broker.getvvmsToDatacentersMapcloudlet.getVmId()
//			}
//		}
//		
//	}
	
//	private Datacenter searchInDcListFor(int key) {
//		for(Datacenter dc : datacenterList) {
//			if(dc.getId() == key) {
//				return dc;
//			}
//		}
//		return null;
//		
//	}
//	
//	private List<MultiTierCloudlet> allCloudletsForDc(Datacenter dc) {
//		for (Vm vm : dc.getgetVmList()) {
//			vm.getCloudletScheduler().get;
//			Host host = new Host();
//			host.getVmScheduler().g
//		}
//		return null;
//		
//	}
}
