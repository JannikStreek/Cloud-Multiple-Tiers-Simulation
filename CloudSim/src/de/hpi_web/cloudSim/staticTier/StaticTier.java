package de.hpi_web.cloudSim.staticTier;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;

import de.hpi_web.cloudSim.staticTier.DatacenterFactory;
import de.hpi_web.cloudSim.utils.OutputWriter;

public class StaticTier {


	/** The cloudlet list. */
	private static List<Cloudlet> cloudletList;

	/** The vmlist. */
	private static List<Vm> vmlist;
	
	public static void main(String[] args) {

		Log.printLine("Starting StaticTier...");
		initializeCloudSim();
		Datacenter datacenter0 = DatacenterFactory.createDatacenter("Datacenter_0", 3);
		Datacenter datacenter1 = DatacenterFactory.createDatacenter("Datacenter_1", 3);
		Datacenter datacenter2 = DatacenterFactory.createDatacenter("Datacenter_2", 3);
		DatacenterBroker broker = createBroker();
		Vm vm = VmFactory.createVm(broker.getId());
		vmlist = new ArrayList<Vm>();
		vmlist.add(vm);

		// submit vm list to the broker
		broker.submitVmList(vmlist);

		// Create Cloudlet(s)
		cloudletList = new ArrayList<Cloudlet>();
		Cloudlet cloudlet = CloudletFactory.createCloudlet(broker.getId(), vm.getId());
		cloudletList.add(cloudlet);

		// submit cloudlet list to the broker
		broker.submitCloudletList(cloudletList);

		CloudSim.startSimulation();
		CloudSim.stopSimulation();

		//Print results
		List<Cloudlet> newList = broker.getCloudletReceivedList();
		OutputWriter.printCloudletList(newList);

		// Print the debt of each user to each datacenter
		datacenter0.printDebts();

		Log.printLine("CloudSimExample1 finished!");
		
	}
	
	public static void initializeCloudSim() {
		int num_user = 1; // number of cloud users
		Calendar calendar = Calendar.getInstance();
		boolean trace_flag = false; // mean trace events
		
		// Initialize the CloudSim library
		CloudSim.init(num_user, calendar, trace_flag);
	}

	/**
	 * Creates the broker.
	 *
	 * @return the datacenter broker
	 */
	private static DatacenterBroker createBroker() {
		DatacenterBroker broker = null;
		try {
			broker = new DatacenterBroker("Broker");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}
	
}
