package de.hpi_web.cloudSim.profiling.utilization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Queue;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;

import de.hpi_web.cloudSim.multitier.staticTier.VmFactory;
import de.hpi_web.cloudSim.profiling.datacenter.UtilWrapper;

import arx.ARX;
import arx.NewArx;

public class UtilManager extends SimEntity {
	
	public static final int RUN = 7000;
	public static final int CLOUDLET_UPDATE = 7001;
	public static final int ROUND_COMPLETED = 7002;
	public static final int UTIL_SIM_FINISHED = 7003;
	
	private int i = 0;
	private int upperThreshold;
	private int lowerThreshold;
	private Map<DatacenterBroker,List<List<Double>>> layers;
    
    private double delay; //seconds
    private List<Integer> brokers;
    private List<Integer> finishedTurnBrokers;
    

	public UtilManager(String name, double delay, int upperThreshold, int lowerThreshold, HashMap<DatacenterBroker, List<List<Double>>> layers) {
		super(name);
		
		this.layers = layers;
		this.brokers = new ArrayList<Integer>();
		this.finishedTurnBrokers = new ArrayList<Integer>();
		
		for (DatacenterBroker d : layers.keySet())
			brokers.add(d.getId());
		
		this.delay = delay;
		this.upperThreshold = upperThreshold;
		this.lowerThreshold = lowerThreshold;
		
		for (DatacenterBroker tier : layers.keySet()) {
			System.out.println("Broker values: "+ layers.get(tier).get(0).size()); 
		}

	}
	


	@Override
	public void startEntity() {
		//sendNow("utilManager", CloudSimTags.REGISTER_RESOURCE, getId());
		schedule(getId(), 10, RUN);
		
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
		}
				
	}

	private void processCompleted(SimEvent ev) {
		int brokerId = (int) ev.getData();
		if (brokers.contains(brokerId) && !finishedTurnBrokers.contains(brokerId)) {
			finishedTurnBrokers.add(brokerId);
			if (finishedTurnBrokers.containsAll(brokers)) {
				finishedTurnBrokers.clear();
				
				try {
					Thread.sleep(new Double(delay*1000).intValue());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Log.printLine(CloudSim.clock() + ": " + getName() + ": Completed Round ");
				if(i < NewArx.RUNNING_VALUES) {
					schedule(getId(), 3, RUN);
				} else {
					//TODO check if it makes a difference if we end each layer here instead of all
					for (DatacenterBroker tier : layers.keySet() )
						schedule(tier.getId(), 3, UTIL_SIM_FINISHED);
				}
			}
		}
	}

	private void processRun(SimEvent ev) {
		Log.printLine(CloudSim.clock() + ": " + getName() + ": UtilManager is running... ");
		
		//for each tier
		for (DatacenterBroker tier : layers.keySet()) {
			//1 = cpu
			//2 = memory
			//3 = disk read
			//4 = disk write
			//5 = network in
			//6 = network out
			List<Double> cpuUtils = layers.get(tier).get(0);
			List<Double> memUtils = layers.get(tier).get(1);
			//schedule(tier.getId(),1, UtilManager.CLOUDLET_UPDATE, cpuUtils.get(i));

			// check if enough vms are present / too much vms present and handle this event => regarding threshold
			int runningVms = tier.getCloudletSubmittedList().size();
			Log.printLine(CloudSim.clock() + ": " + getName() + ": Vms running: " + runningVms);
			double cpuUtilizationPerVm = (cpuUtils.get(i)/(double)runningVms);
			double memUtilizationPerVm = (memUtils.get(i)/(double)runningVms);
			Log.printLine("Current CPU Util: " + cpuUtilizationPerVm);
			Log.printLine("Running Vms: " + runningVms);
			
			if ((cpuUtilizationPerVm > this.upperThreshold && runningVms > 0) ||
				(memUtilizationPerVm > 200000.0 && runningVms > 0)) {
				// create new vm
				Log.printLine("Too few Vms... creating");
				Vm v = VmFactory.createVm(tier.getId());
				schedule(tier.getId(), 1, CloudSimTags.VM_CREATE, v);
			}
			else if ((cpuUtilizationPerVm < this.lowerThreshold && runningVms > 1) &&
					  (memUtilizationPerVm < 100000.0 && runningVms > 1)) {
				// destroy vm
				Log.printLine("Too many Vms... destroying");
				schedule(tier.getId(), 1, CloudSimTags.VM_DESTROY);
			}
			//update utilization of all cloudlets
			UtilWrapper wrapper = new UtilWrapper(cpuUtils.get(i), memUtils.get(i), 0.0,0.0,0.0,0.0);
			
			schedule(tier.getId(), 2, UtilManager.CLOUDLET_UPDATE, wrapper);
		}
		this.i++;
		
	}
	

	@Override
	public void shutdownEntity() {
		// TODO Auto-generated method stub
		
	}

}
