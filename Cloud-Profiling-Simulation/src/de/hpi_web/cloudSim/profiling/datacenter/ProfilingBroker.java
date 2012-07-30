package de.hpi_web.cloudSim.profiling.datacenter;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;

import de.hpi_web.cloudSim.profiling.observer.Observable;
import de.hpi_web.cloudSim.profiling.observer.Observer;
import de.hpi_web.cloudSim.profiling.utilization.UtilManager;
import de.hpi_web.cloudSim.profiling.utilization.UtilWrapper;
import de.hpi_web.cloudSim.profiling.utilization.UtilizationModelFixed;

public class ProfilingBroker extends DatacenterBroker implements Observable{
	
//	private List<Cloudlet> cloudlets;
	private List<Observer> observers;
	private List<Integer> dcAffinity;
	private int amount = 0;
	private int pricePerVm = 10;
	private int MinPerTick = 10;

	public ProfilingBroker(String name) throws Exception {
		super(name);
		observers = new ArrayList<Observer>();
//		cloudlets = new ArrayList<Cloudlet>();
		dcAffinity = new ArrayList<Integer>();
	}
	public void setDcAffinityList(List<Integer> dcAffinity) {
		this.dcAffinity = dcAffinity;
	}
	public List<Integer> getDcAffinityList() {
		return dcAffinity;
	}
	public void addAffinity(int datacenterId) {
		if (!dcAffinity.contains(datacenterId))
			dcAffinity.add(datacenterId);
	}
	
	public void processOtherEvent(SimEvent ev) {
		switch (ev.getTag()) {
			case UtilManager.CLOUDLET_UPDATE:
				processCloudletUpdate(ev);
				break;
			case UtilManager.UTIL_SIM_FINISHED:
				processUtilFinished(ev);
				break;
			case CloudSimTags.VM_CREATE:
				processNewVm(ev);
				break;
			case CloudSimTags.VM_DESTROY:
				processDestroyVm(ev);
				break;
		}
	}
	
	private void processDestroyVm(SimEvent ev) {
		// pick one VM and destroy it. Doesnt matter which one
		// TODO could pick VM through cloudlet list... getCloudletSubmittedList().get(0).getVmId()
		if (getVmsCreatedList().isEmpty())
			return;
		Vm vm = getVmsCreatedList().get(0);
		for (Cloudlet c : getCloudletSubmittedList()) {
			if (c.getVmId() == vm.getId()) {
				getCloudletSubmittedList().remove(c);
				cloudletsSubmitted--;
				break;
			}
		}
		Log.printLine(CloudSim.clock() + ": " + getName() + ": Destroying VM #" + vm.getId());
		sendNow(getVmsToDatacentersMap().get(vm.getId()), CloudSimTags.VM_DESTROY, vm);

		getVmsCreatedList().remove(0);
	}
	
	private void addAmount() {
		this.amount += this.pricePerVm*this.MinPerTick;
		
	}
	public Vm getVmForVmId(int vmId) {
		for(Vm vm : getVmList()) {
			if(vm.getId() == vmId) {
			  return vm;
			}
		}
		return null;
		
	}

	private void processNewVm(SimEvent ev) {
		Vm vm = (Vm) ev.getData();
		getVmList().add(vm);
		// TODO choose datacenter here
		int datacenterId;
		if (getDcAffinityList().isEmpty()) {
			datacenterId = getDatacenterIdsList().get(0);
		} else {
			datacenterId = getDcAffinityList().get(0);
		}
		
		String datacenterName = CloudSim.getEntityName(datacenterId);
		if (!getVmsToDatacentersMap().containsKey(vm.getId())) {
			Log.printLine(CloudSim.clock() + ": " + getName() + ": Trying to Create VM #" + vm.getId()
					+ " in " + datacenterName);
			schedule(datacenterId, 0, CloudSimTags.VM_CREATE_ACK, vm);
			//sendNow(datacenterId, CloudSimTags.VM_CREATE_ACK, vm);
		}

		getDatacenterRequestedIdsList().add(datacenterId);
		
		setVmsRequested(getVmsRequested()+1);
		setVmsAcks(getVmsAcks()+1);
	}

	private void processUtilFinished(SimEvent ev) {
		Log.printLine(CloudSim.clock() + ": " + getName() + ": Finishing ");
		for (int datacenter : getDatacenterIdsList()) {
			sendNow(datacenter, UtilManager.UTIL_SIM_FINISHED, null);
		}
		
	}

	private void processCloudletUpdate(SimEvent ev) {
		Log.printLine(CloudSim.clock() + ": " + getName() + ": Updating Cloudlets for next round ");
		UtilWrapper wrapper = (UtilWrapper) ev.getData();
		if(cloudletsSubmitted < getVmsCreatedList().size()) {
			List<Integer> vmsWithCloudletIds = getVmsWithCloudletIds();
			List<Vm> vms = getVmsCreatedList();
			List<Vm> missingCloudletVms = getMissingCloudletVms(vmsWithCloudletIds, vms);
			for (Vm vm : missingCloudletVms) {
			    Cloudlet cloudlet = createCloudlet(vm, wrapper);
				cloudlet.setVmId(vm.getId());
				sendNow(getVmsToDatacentersMap().get(vm.getId()), CloudSimTags.CLOUDLET_SUBMIT, cloudlet);
				//cloudlets.add(cloudlet);
				getCloudletSubmittedList().add(cloudlet);
				cloudletsSubmitted++;
				// remove submitted cloudlets from waiting list
				for (Cloudlet submittedCloudlet : getCloudletSubmittedList()) {
					getCloudletList().remove(submittedCloudlet);
				}
			}
			
		}
		
		for (Cloudlet cloudlet : getCloudletSubmittedList()) {
			double cloudletsSubmittedDouble = (double)cloudletsSubmitted;
			
			ProfilingCloudlet pcloudlet = (ProfilingCloudlet) cloudlet;
			
			//save the ticks for this cloudlet, maybe this information will be used in the future
			pcloudlet.incrementTicks();

			//calculate new amount value for this layer
			addAmount();
			
			pcloudlet.setUtilizationModelCpu(new UtilizationModelFixed(wrapper.getCpuUtil()/cloudletsSubmittedDouble));
			pcloudlet.setUtilizationModelRam(new UtilizationModelFixed(wrapper.getMemUtil()/cloudletsSubmittedDouble));
			pcloudlet.setUtilizationModelDiskRead(new UtilizationModelFixed(wrapper.getDiskReadUtil()/cloudletsSubmittedDouble));
			pcloudlet.setUtilizationModelDiskWrite(new UtilizationModelFixed(wrapper.getDiskWriteUtil()/cloudletsSubmittedDouble));
			pcloudlet.setUtilizationModelBw(new UtilizationModelFixed(wrapper.getBwInUtil()/cloudletsSubmittedDouble));
			pcloudlet.setUtilizationModelBwOut(new UtilizationModelFixed(wrapper.getBwOutUtil()/cloudletsSubmittedDouble));
			
		}
		
//		for(Cloudlet cloudlet : cloudletSubmittedList) {
//			ProfilingCloudlet pcloudlet = (ProfilingCloudlet) cloudlet;
//			Log.printLine(CloudSim.clock() + ": " + getName() + " : at VM" + cloudlet.getVmId() + " : cloudlet at CPU util  "+ pcloudlet.getUtilizationOfCpu(CloudSim.clock()));
//			Log.printLine(CloudSim.clock() + ": " + getName() + " : at VM" + cloudlet.getVmId() + " : cloudlet at Mem util  "+ pcloudlet.getUtilizationOfRam(CloudSim.clock()));
//			Log.printLine(CloudSim.clock() + ": " + getName() + " : at VM" + cloudlet.getVmId() + " : cloudlet at Disk Read util  "+ pcloudlet.getUtilizationOfDiskRead(CloudSim.clock()));
//			Log.printLine(CloudSim.clock() + ": " + getName() + " : at VM" + cloudlet.getVmId() + " : cloudlet at Disk Write util  "+ pcloudlet.getUtilizationOfDiskWrite(CloudSim.clock()));
//			Log.printLine(CloudSim.clock() + ": " + getName() + " : at VM" + cloudlet.getVmId() + " : cloudlet at Bw In util  "+ pcloudlet.getUtilizationOfBw(CloudSim.clock()));
//			Log.printLine(CloudSim.clock() + ": " + getName() + " : at VM" + cloudlet.getVmId() + " : cloudlet at Bw Out util  "+ pcloudlet.getUtilizationOfBwOut(CloudSim.clock()));
//		}
		
		//change gui
		notifyObservers();
		
		//proceed if all brokers have finished their execution
	    sendNow(ev.getSource(), UtilManager.ROUND_COMPLETED, getId());
	}
	
	@Override
	protected void processResourceCharacteristics(SimEvent ev) {
		DatacenterCharacteristics characteristics = (DatacenterCharacteristics) ev.getData();
		getDatacenterCharacteristicsList().put(characteristics.getId(), characteristics);

		if (getDatacenterCharacteristicsList().size() == getDatacenterIdsList().size()) {
			setDatacenterRequestedIdsList(new ArrayList<Integer>());
			if(dcAffinity.isEmpty())
				createVmsInDatacenter(getDatacenterIdsList().get(0));
			else {
				for (int id : getDatacenterIdsList()) {
					// TODO: maybe use another datacenter?
					if (dcAffinity.contains(id)) {
						createVmsInDatacenter(id);
					}
				}
			}
		}
	}
	
	private List<Vm> getMissingCloudletVms(List<Integer> vmsWithCloudletIds,
			List<Vm> vms) {
		List<Vm> vmsWithoutCloudlet = new ArrayList<Vm>();
		//check every vm for a cloudlet
		for (Vm vm : vms) {
			//does the vm have a cloudlet?
			boolean toAdd = true;
			for (int vmId :  vmsWithCloudletIds) {
				if(vmId == vm.getId()) {
					toAdd = false;
					break;
				}
			}
			
			//vm has no cloudlet yet, so save it
			if(toAdd)
			  vmsWithoutCloudlet.add(vm);
		}
		return vmsWithoutCloudlet;
	}

	private List<Integer> getVmsWithCloudletIds() {
		List<Integer> vmIds = new ArrayList<Integer>();
		for (Cloudlet cloudlet : getCloudletSubmittedList()) {
			vmIds.add(cloudlet.getVmId());
		}
		return vmIds;
	}

	private Cloudlet createCloudlet(Vm vm, UtilWrapper wrapper) {
		Log.printLine(CloudSim.clock() + ": " + getName() + ": Creating Cloudlet ");
		
	  // Cloudlet properties
	  int id = 0;
	  int pesNumber = 1;
	  long length = 100000000; //TODO calc it
	  double createdVms = (double)getVmsCreatedList().size();
	  
	  double cpuUtilizationPerVm = (wrapper.getCpuUtil()/createdVms);
	  double memUtilizationPerVm = (wrapper.getMemUtil()/createdVms);
	  double diskReadUtilizationPerVm = (wrapper.getDiskReadUtil()/createdVms);
	  double diskWriteUtilizationPerVm = (wrapper.getDiskWriteUtil()/createdVms);
	  double bwInUtilizationPerVm = (wrapper.getBwInUtil()/createdVms);
	  double bwOutUtilizationPerVm = (wrapper.getBwOutUtil()/createdVms);
	  //double cpuUtilizationPerVm = (wrapper.getCpuUtil()/(double)getVmsCreatedList().size());// util = 1 means 100% utilization

	  long fileSize = 300;
	  long outputSize = 300;
	  UtilizationModel cpuUtilizationModel = new UtilizationModelFixed(cpuUtilizationPerVm);
	  UtilizationModel memUtilizationModel = new UtilizationModelFixed(memUtilizationPerVm);
	  UtilizationModel diskReadUtilizationModel = new UtilizationModelFixed(diskReadUtilizationPerVm);
	  UtilizationModel diskWriteUtilizationModel = new UtilizationModelFixed(diskWriteUtilizationPerVm);
	  UtilizationModel bwInUtilizationModel = new UtilizationModelFixed(bwInUtilizationPerVm);
	  UtilizationModel bwOutUtilizationModel = new UtilizationModelFixed(bwOutUtilizationPerVm);
	  Cloudlet cloudlet = new ProfilingCloudlet(id, length, pesNumber, fileSize, outputSize, cpuUtilizationModel, memUtilizationModel, diskReadUtilizationModel,diskWriteUtilizationModel,bwInUtilizationModel,bwOutUtilizationModel);
	  cloudlet.setUserId(getId());
	  return cloudlet;
	}

	@Override
	public void notifyObservers() {
		for(Observer obs : this.observers) {
			obs.refreshData(this);
		}
		
	}
	
	public int getAmount() {
		return amount;
	}

	@Override
	public void register(Observer obs) {
		observers.add(obs);
		
	}

	@Override
	public void unRegister(Observer obs) {
		observers.remove(obs);
		
	}

}
