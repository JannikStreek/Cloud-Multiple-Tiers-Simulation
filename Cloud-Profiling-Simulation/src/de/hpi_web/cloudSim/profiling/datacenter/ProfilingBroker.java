package de.hpi_web.cloudSim.profiling.datacenter;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;

import de.hpi_web.cloudSim.profiling.utilization.UtilManager;
import de.hpi_web.cloudSim.profiling.utilization.UtilizationModelFixed;

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
			case UtilManager.CLOUDLET_UPDATE:
				processCloudletUpdate(ev);
				break;
			case UtilManager.UTIL_SIM_FINISHED:
				processUtilFinished(ev);
				break;
				
		}
	}
	
	private void processUtilFinished(SimEvent ev) {
		for (int datacenter : getDatacenterIdsList()) {
			sendNow(datacenter, UtilManager.ROUND_COMPLETED, null);
		}
		
	}

	private void processCloudletUpdate(SimEvent ev) {
		
		int cpuUtil = Integer.parseInt(ev.getData().toString());
		if(cloudletsSubmitted == 0) {
			
			List<Vm> vms = getVmsCreatedList();
			for (Vm vm : vms) {
			    Cloudlet cloudlet = createCloudlet(vm, cpuUtil);
				cloudlet.setVmId(vm.getId());
				sendNow(getVmsToDatacentersMap().get(vm.getId()), CloudSimTags.CLOUDLET_SUBMIT, cloudlet);
				cloudlets.add(cloudlet);
				cloudletsSubmitted++;
				getCloudletSubmittedList().add(cloudlet);
				// remove submitted cloudlets from waiting list
				for (Cloudlet submittedCloudlet : getCloudletSubmittedList()) {
					getCloudletList().remove(submittedCloudlet);
				}
			}
			
		} else {
			for (Cloudlet cloudlet : cloudlets) {
				cloudlet.setUtilizationModelCpu(new UtilizationModelFixed(cpuUtil)); //TODO value...
			}
		}

	  sendNow(ev.getSource(), UtilManager.ROUND_COMPLETED, null);
	}

	private Cloudlet createCloudlet(Vm vm, int cpuUtil) {
	  // Cloudlet properties
	  int id = 0;
	  int pesNumber = 1;
	  long length = 400000; //TODO calc it
	  /*
	   * CloudSim has MIPS concept for describing the CPU throughput! What we are interested in is CPU utilization,
	   * so make an assumption about the VM capacity (e.g., 1000 MIPS),
	   * then calculate the throughput at each moment from the CPU utilization calculated by models.
	   * For instance if CPU utilization is 50%, then the MIPS for this instance at this time is 500.
	   */
	  long fileSize = 300;
	  long outputSize = 300;
	  UtilizationModel utilizationModel = new UtilizationModelFixed(2);
	  Cloudlet cloudlet = new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
	  cloudlet.setUserId(getId());
	  return cloudlet;
	}

}
