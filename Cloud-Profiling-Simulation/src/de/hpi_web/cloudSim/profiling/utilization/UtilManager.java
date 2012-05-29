package de.hpi_web.cloudSim.profiling.utilization;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;

public class UtilManager extends SimEntity{
	
	private static int RUN = 7000;
	
	//TODO first test with fixed values
    List cpuUtil = new ArrayList<Integer>();
    

	public UtilManager(String name) {
		super(name);
		cpuUtil.add(30);
		cpuUtil.add(70);
		cpuUtil.add(130);
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
			case CloudSimTags.CLOUDLET_SUBMIT_ACK:
				processRun(ev);
				break;
		}
				
	}

	private void processRun(SimEvent ev) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdownEntity() {
		// TODO Auto-generated method stub
		
	}

}
