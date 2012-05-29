package de.hpi_web.cloudSim.profiling.utilization;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;

public class UtilManager extends SimEntity{
	
	public static final int RUN = 7000;
	public static final int CLOUDLET_UPDATE = 7001;
	public static final int ROUND_COMPLETED = 7002;
	public static final int UTIL_SIM_FINISHED = 7003;
	private int brokerId;


	//TODO first test with fixed values
    List<Integer> cpuUtil = new ArrayList<Integer>();
    private int counter;
    

	public UtilManager(String name) {
		super(name);
		cpuUtil.add(30);
		cpuUtil.add(70);
		cpuUtil.add(130);
		counter = 0;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void startEntity() {
		//sendNow("utilManager", CloudSimTags.REGISTER_RESOURCE, getId());
		schedule(getId(), 0, RUN);
		
	}

	@Override
	public void processEvent(SimEvent ev) {

		switch (ev.getTag()) {
			case UtilManager.RUN:
				processRun(ev);
				break;
			case UtilManager.ROUND_COMPLETED:
				processCompleted(ev);
				break;
//			case UtilManager.CLOUDLET_CREATION:
//				processCloudletCreation(ev);
//				break;
		}
				
	}

	private void processCompleted(SimEvent ev) {
		Log.printLine(CloudSim.clock() + ": " + getName() + ": Completed Round ");
		
	}

	private void processRun(SimEvent ev) {
		Log.printLine(CloudSim.clock() + ": " + getName() + ": UtilManager is running... ");
		int cpu = Integer.parseInt(cpuUtil.remove(counter).toString());
		counter++;
		
		//TODO check if enough vms are present / too much vms present and handle this event
		//schedule ...
		
		//TODO calc new util
		
		sendNow(brokerId, UtilManager.CLOUDLET_UPDATE, cpu);
		
		if(counter < cpuUtil.size()) {
			schedule(getId(), 1, RUN);
		} else {
			schedule(brokerId, 1, UTIL_SIM_FINISHED);
		}
		
	}
	


	@Override
	public void shutdownEntity() {
		// TODO Auto-generated method stub
		
	}
	
	public int getBrokerId() {
		return brokerId;
	}

	public void setBrokerId(int brokerId) {
		this.brokerId = brokerId;
	}

}
