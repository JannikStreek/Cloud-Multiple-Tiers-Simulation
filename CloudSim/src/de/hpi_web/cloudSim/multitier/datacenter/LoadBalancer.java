package de.hpi_web.cloudSim.multitier.datacenter;

import java.util.List;

import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Vm;

public abstract class LoadBalancer {

	protected DatacenterBroker broker;
	
	LoadBalancer(DatacenterBroker broker) {
		this.broker = broker;
	}
	abstract public Vm getNextVm();
	public List<Vm> getVms() {
		return broker.getVmsCreatedList();
	}
}
