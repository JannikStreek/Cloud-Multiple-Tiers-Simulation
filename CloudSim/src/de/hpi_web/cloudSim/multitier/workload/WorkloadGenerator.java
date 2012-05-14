package de.hpi_web.cloudSim.multitier.workload;

import org.cloudbus.cloudsim.DatacenterBroker;


public abstract class WorkloadGenerator {
	public static final int LOAD_MIN = 10;
	public static final int LOAD_MAX = 100;

	public abstract void scheduleWorkloadForBroker(DatacenterBroker broker, double timeLimit);
	public abstract void scheduleWorkloadForBroker(DatacenterBroker broker);
}
