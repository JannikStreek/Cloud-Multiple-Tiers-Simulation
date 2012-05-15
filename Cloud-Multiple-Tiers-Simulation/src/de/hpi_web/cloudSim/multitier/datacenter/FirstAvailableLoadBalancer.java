package de.hpi_web.cloudSim.multitier.datacenter;

import java.util.List;

import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Vm;

public class FirstAvailableLoadBalancer extends LoadBalancer {

	FirstAvailableLoadBalancer(DatacenterBroker b) {
		super(b);
	}

	@Override
	public Vm getNextVm() {
		return getVms().get(0);
	}

}
