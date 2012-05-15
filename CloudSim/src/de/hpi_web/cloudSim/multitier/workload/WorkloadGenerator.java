package de.hpi_web.cloudSim.multitier.workload;

import org.cloudbus.cloudsim.DatacenterBroker;

import de.hpi_web.cloudSim.multitier.datacenter.DatacenterAffinityBroker;


public abstract class WorkloadGenerator {
	public static final int LOAD_MIN = 5;
	public static final int LOAD_MAX = 100;

	public abstract void scheduleWorkloadForBroker(DatacenterAffinityBroker broker, double timeLimit);
}
