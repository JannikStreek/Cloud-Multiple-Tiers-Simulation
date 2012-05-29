package de.hpi_web.cloudSim.profiling.utilization;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;

public class UtilManager extends SimEntity{
	
	private static final int RUN = 7000;
	
	//TODO first test with fixed values
    List cpuUtil = new ArrayList<Integer>();
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
		}
				
	}

	private void processRun(SimEvent ev) {
		int cpu = Integer.parseInt(cpuUtil.remove(counter).toString());
		counter++;
		
		//TODO check if enough vms are present / too much vms present and handle this event
		//schedule ...
		//TODO create cloudlets for these vms
		//schedule ...
		
		if(counter < cpuUtil.size()) {
			schedule(getId(), 0, RUN);
		}
		
	}

	@Override
	public void shutdownEntity() {
		// TODO Auto-generated method stub
		
	}

}
