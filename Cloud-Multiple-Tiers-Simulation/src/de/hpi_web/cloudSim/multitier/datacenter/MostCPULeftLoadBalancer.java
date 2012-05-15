package de.hpi_web.cloudSim.multitier.datacenter;

import java.util.List;

import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Vm;

public class MostCPULeftLoadBalancer extends LoadBalancer {

	MostCPULeftLoadBalancer(DatacenterBroker broker) {
		super(broker);
	}

	@Override
	public Vm getNextVm() {
		List<Vm> vmList = getVms();
		Vm bestPick = vmList.get(0);
		for (Vm v : vmList) {
			if (v.getMips() - v.getCurrentRequestedTotalMips() > bestPick.getMips() - bestPick.getCurrentRequestedTotalMips())
				bestPick = v;
		}
		return bestPick;
	}
}
