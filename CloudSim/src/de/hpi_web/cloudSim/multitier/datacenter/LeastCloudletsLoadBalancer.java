package de.hpi_web.cloudSim.multitier.datacenter;

import java.util.List;

import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Vm;

public class LeastCloudletsLoadBalancer extends LoadBalancer {

	LeastCloudletsLoadBalancer(DatacenterBroker broker) {
		super(broker);
	}

	@Override
	public Vm getNextVm() {
		List<Vm> vmList = getVms();
		Vm bestPick = vmList.get(0);
		for (Vm v : vmList) {
			if (v.getCloudletScheduler().runningCloudlets() < bestPick.getCloudletScheduler().runningCloudlets())
				bestPick = v;
		}
		return bestPick;
	}

}
