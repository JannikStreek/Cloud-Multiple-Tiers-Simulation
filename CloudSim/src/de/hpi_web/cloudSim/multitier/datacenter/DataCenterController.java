package de.hpi_web.cloudSim.multitier.datacenter;

import java.util.Map;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.lists.VmList;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import de.hpi_web.cloudSim.multitier.MultiTierCloudlet;
/*
 * DataCenterController
 * Override default behavior and limit the datacenter => vm mapping to only one possible datacenter.
 * Despite that properties/behaviour is introduced to fit the multiple tier architecture.
 */
public class DataCenterController extends DatacenterBroker {
	
	private int tier;
	
	//TODO how to disallow multiple datacenters?
	
	public DataCenterController(String name, int tier) throws Exception {
		super(name);
		this.tier = tier;
	}
	
	@Override
	public void processOtherEvent(SimEvent ev) {
		Object payload = ev.getData();
		
		//TODO process new events like request/response
		
        switch (this.tier) {
        case 1:  this.tier = MultiTierCloudlet.TIER_SERVER;
                 break;
        case 2:  this.tier = MultiTierCloudlet.TIER_APP;
                 break;
        case 3:  this.tier = MultiTierCloudlet.TIER_DB;
                 break;
        default: this.tier = 0;
                 break;

        }
	}
	
/*	private void createMultiTierCloudlet(int tier, int src, int dest) {
		
		int id = 0;
        long length = 400000;
        long fileSize = 300;
        long outputSize = 300;
        UtilizationModel utilizationModel = new UtilizationModelFull();

        MultiTierCloudlet cloudlet = new MultiTierCloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
        cloudlet.setTier(tier);
        CloudSim.send(src, dest, delay, tag, cloudlet);
        Host
		
	}*/
	
////////////////////////////////////////////////////////////////////////////////////
///Methods which have to be overridden to realize a Broker 1:1 datacenter Mapping///
////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void submitCloudlets() {
		int vmIndex = 0;
		for (Cloudlet cloudlet : getCloudletList()) {
			Vm vm;
			// if user didn't bind this cloudlet and it has not been executed yet
			if (cloudlet.getVmId() == -1) {
				vm = getVmsCreatedList().get(vmIndex);
			} else { // submit to the specific vm
				vm = VmList.getById(getVmsCreatedList(), cloudlet.getVmId());
				if (vm == null) { // vm was not created
					Log.printLine(CloudSim.clock() + ": " + getName() + ": Postponing execution of cloudlet "
							+ cloudlet.getCloudletId() + ": bount VM not available");
					continue;
				}
			}

			Log.printLine(CloudSim.clock() + ": " + getName() + ": Sending cloudlet "
					+ cloudlet.getCloudletId() + " to VM #" + vm.getId());
			cloudlet.setVmId(vm.getId());
			sendNow(datacenterIdsList.get(0), CloudSimTags.CLOUDLET_SUBMIT, cloudlet);
			cloudletsSubmitted++;
			vmIndex = (vmIndex + 1) % getVmsCreatedList().size();
			getCloudletSubmittedList().add(cloudlet);
		}

		// remove submitted cloudlets from waiting list
		for (Cloudlet cloudlet : getCloudletSubmittedList()) {
			getCloudletList().remove(cloudlet);
		}
	}
	
	/**
	 * Destroy the virtual machines running in datacenters.
	 * 
	 * @pre $none
	 * @post $none
	 */
	@Override
	protected void clearDatacenters() {
		for (Vm vm : getVmsCreatedList()) {
			Log.printLine(CloudSim.clock() + ": " + getName() + ": Destroying VM #" + vm.getId());
			sendNow(datacenterIdsList.get(0), CloudSimTags.VM_DESTROY, vm);
		}

		getVmsCreatedList().clear();
	}
	
	@Override
	protected Map<Integer, Integer> getVmsToDatacentersMap() {
		throw new NotImplementedException();
	}
	
	/**
	 * Create the virtual machines in a datacenter.
	 * 
	 * @param datacenterId Id of the chosen PowerDatacenter
	 * @pre $none
	 * @post $none
	 */
	@Override
	protected void createVmsInDatacenter(int datacenterId) {
		datacenterId = 0; //TODO Override input to map the requested behaviour, but bad code
		// send as much vms as possible for this datacenter before trying the next one
		int requestedVms = 0;
		String datacenterName = CloudSim.getEntityName(datacenterId);
		for (Vm vm : getVmList()) {
			if (!getVmsToDatacentersMap().containsKey(vm.getId())) {
				Log.printLine(CloudSim.clock() + ": " + getName() + ": Trying to Create VM #" + vm.getId()
						+ " in " + datacenterName);
				sendNow(datacenterId, CloudSimTags.VM_CREATE_ACK, vm);
				requestedVms++;
			}
		}

		getDatacenterRequestedIdsList().add(datacenterId);

		setVmsRequested(requestedVms);
		setVmsAcks(0);
	}
	
	/**
	 * Process the ack received due to a request for VM creation.
	 * 
	 * @param ev a SimEvent object
	 * @pre ev != null
	 * @post $none
	 */
	@Override
	protected void processVmCreate(SimEvent ev) {
		int[] data = (int[]) ev.getData();
		int datacenterId = 0; //TODO override
		int vmId = data[1];
		int result = data[2];

		if (result == CloudSimTags.TRUE) {
			getVmsToDatacentersMap().put(vmId, datacenterId);
			getVmsCreatedList().add(VmList.getById(getVmList(), vmId));
			Log.printLine(CloudSim.clock() + ": " + getName() + ": VM #" + vmId
					+ " has been created in Datacenter #" + datacenterId + ", Host #"
					+ VmList.getById(getVmsCreatedList(), vmId).getHost().getId());
		} else {
			Log.printLine(CloudSim.clock() + ": " + getName() + ": Creation of VM #" + vmId
					+ " failed in Datacenter #" + datacenterId);
		}

		incrementVmsAcks();

		// all the requested VMs have been created
		if (getVmsCreatedList().size() == getVmList().size() - getVmsDestroyed()) {
			submitCloudlets();
		} else {
			// all the acks received, but some VMs were not created
			if (getVmsRequested() == getVmsAcks()) {
				// find id of the next datacenter that has not been tried
				for (int nextDatacenterId : getDatacenterIdsList()) {
					if (!getDatacenterRequestedIdsList().contains(nextDatacenterId)) {
						createVmsInDatacenter(nextDatacenterId);
						return;
					}
				}

				// all datacenters already queried
				if (getVmsCreatedList().size() > 0) { // if some vm were created
					submitCloudlets();
				} else { // no vms created. abort
					Log.printLine(CloudSim.clock() + ": " + getName()
							+ ": none of the required VMs could be created. Aborting");
					finishExecution();
				}
			}
		}
	}

}
