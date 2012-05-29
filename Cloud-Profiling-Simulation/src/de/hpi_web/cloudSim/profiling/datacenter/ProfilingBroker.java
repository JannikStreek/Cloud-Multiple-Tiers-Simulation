package de.hpi_web.cloudSim.profiling.datacenter;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;

import de.hpi_web.cloudSim.profiling.utilization.UtilManager;

public class ProfilingBroker extends DatacenterBroker{
	
	private List<Cloudlet> cloudlets;

	public ProfilingBroker(String name) throws Exception {
		super(name);
		cloudlets = new ArrayList<Cloudlet>();
		// TODO Auto-generated constructor stub
	}
	
	public void processOtherEvent(SimEvent ev) {
		switch (ev.getTag()) {
		// Resource characteristics request
			case UtilManager.CLOUDLET_CREATION:
				processCloudletCreation(ev);
				break;
		}
	}
	
	private void processCloudletCreation(SimEvent ev) {
		
		//Finish old cloudlets
//		for (Cloudlet cloudlet : cloudlets) {
//			vmList.get(cloudlet.getVmId()).getCloudletScheduler().setCloudletStatus(Cloudlet.SUCCESS);
//			
//		}

		List<Vm> vms = getVmsCreatedList();
		for (Vm vm : vms) {
		    Cloudlet cloudlet = createCloudlet();
			cloudlet.setVmId(vm.getId());
			sendNow(getVmsToDatacentersMap().get(vm.getId()), CloudSimTags.CLOUDLET_SUBMIT, cloudlet);
			cloudlets.add(cloudlet);
			cloudletsSubmitted++;
			getCloudletSubmittedList().add(cloudlet);
			// remove submitted cloudlets from waiting list
			for (Cloudlet cloudlet : getCloudletSubmittedList()) {
				getCloudletList().remove(cloudlet);
			}
		}

	  sendNow(ev.getSource(), UtilManager.ROUND_COMPLETED, null);
	}

	private Cloudlet createCloudlet() {
		  // Cloudlet properties
		  int id = 0;
		  int pesNumber = 1;
		  long length = 400000;
		  long fileSize = 300;
		  long outputSize = 300;
		  UtilizationModel utilizationModel = new UtilizationModelFull();
		  Cloudlet cloudlet = new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
		  cloudlet.setUserId(getId());
		return null;
	}

}
