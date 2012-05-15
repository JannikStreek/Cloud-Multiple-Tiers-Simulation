package de.hpi_web.cloudSim.multitier.workload;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;

import de.hpi_web.cloudSim.multitier.datacenter.DatacenterAffinityBroker;


public abstract class WorkloadGenerator extends SimEntity {
	public static final int START_WORKLOAD_SCHEDULING = 1001;
	private DatacenterAffinityBroker targetBroker;
	private double timeLimit; 
	
	public WorkloadGenerator(String name, DatacenterAffinityBroker targetBroker, double timeLimit) {
		super(name);
		this.targetBroker = targetBroker;
		this.timeLimit = timeLimit;
	}

	public static final int LOAD_MIN = 5;
	public static final int LOAD_MAX = 100;

	public abstract void scheduleWorkloadForBroker(DatacenterAffinityBroker broker, double timeLimit);
	@Override
	public void startEntity() {
		// TODO: find something better than this delay workaround, consider delay for workload scheduling
		schedule(getId(), 1, START_WORKLOAD_SCHEDULING);
	}
	@Override
	public void processEvent(SimEvent ev) {

		switch (ev.getTag()) {
//		// Resource characteristics request
			case START_WORKLOAD_SCHEDULING:
				scheduleWorkloadForBroker(targetBroker, timeLimit);
				break;
			// if the simulation finishes
			case CloudSimTags.END_OF_SIMULATION:
				shutdownEntity();
				break;
//			// other unknown tags are processed by this method
//			default:
//				processOtherEvent(ev);
//				break;
		}
	}
	@Override
	public void shutdownEntity() {
		Log.printLine(getName() + " is shutting down...");
	}
}
