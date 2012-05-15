package de.hpi_web.cloudSim.multitier.example;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;

import de.hpi_web.cloudSim.multitier.MultiTierCloudTags;
import de.hpi_web.cloudSim.multitier.MultiTierCloudlet;
import de.hpi_web.cloudSim.multitier.datacenter.DatacenterAffinityBroker;
import de.hpi_web.cloudSim.multitier.staticTier.CloudletFactory;
import de.hpi_web.cloudSim.multitier.staticTier.DatacenterFactory;
import de.hpi_web.cloudSim.multitier.staticTier.VmFactory;
import de.hpi_web.cloudSim.multitier.workload.SpikeWorkloadGenerator;
import de.hpi_web.cloudSim.utils.OutputWriter;

public class StaticTier {
	
	public static void main(String[] args) {

		Log.printLine("Starting StaticTier...");
		initializeCloudSim();
		Datacenter wsDatacenter = DatacenterFactory.createDatacenter("WebserverCenter", 0, 3);
		Datacenter appDatacenter = DatacenterFactory.createDatacenter("ApplicationCenter", 3, 3);
		Datacenter dbDatacenter = DatacenterFactory.createDatacenter("DatabaseCenter", 6, 3);
//		DatacenterBroker wsBroker = createBroker("wsBroker");
//		DatacenterBroker appBroker = createBroker("appBroker");
//		DatacenterBroker dbBroker = createBroker("dbBroker");
		DatacenterAffinityBroker wsBroker = createBroker("wsBroker");
		DatacenterAffinityBroker appBroker = createBroker("appBroker");
		DatacenterAffinityBroker dbBroker = createBroker("dbBroker");
		
		//wsBroker.setSuccessor(appBroker);
		//appBroker.setSuccessor(dbBroker);
		
		List<Vm> wsVms = VmFactory.createVms(0, 3, wsBroker.getId());
		List<Vm> appVms = VmFactory.createVms(3, 3, appBroker.getId());
		List<Vm> dbVms = VmFactory.createVms(6, 3, dbBroker.getId());
		
		List<Integer> wsDcAffinity = new ArrayList<Integer>();
		List<Integer> appDcAffinity = new ArrayList<Integer>();
		List<Integer> dbDcAffinity = new ArrayList<Integer>();
		
		wsDcAffinity.add(wsDatacenter.getId());
		appDcAffinity.add(appDatacenter.getId());
		dbDcAffinity.add(dbDatacenter.getId());
		
		wsBroker.setDcAffinityList(wsDcAffinity);
		appBroker.setDcAffinityList(appDcAffinity);
		dbBroker.setDcAffinityList(dbDcAffinity);
		// submit vm lists to the brokers
		wsBroker.submitVmList(wsVms);
		appBroker.submitVmList(appVms);
		dbBroker.submitVmList(dbVms);

		List<MultiTierCloudlet> wsCloudlets = CloudletFactory.createCloudlets(0, 50, wsBroker.getId());
		//List<MultiTierCloudlet> appCloudlets = CloudletFactory.createCloudlets(10, 5, appBroker.getId());
		//List<MultiTierCloudlet> dbCloudlets = CloudletFactory.createCloudlets(20, 2, dbBroker.getId());

		//wsBroker.submitCloudletList(wsCloudlets);
		//appBroker.submitCloudletList(appCloudlets);
		//dbBroker.submitCloudletList(dbCloudlets);
		SpikeWorkloadGenerator workloadGen = new SpikeWorkloadGenerator("WorkloadGenerator", wsBroker, 100.0);
		
		//System.exit(0);
		CloudSim.startSimulation();
		CloudSim.stopSimulation();

		//Print results
		List<Cloudlet> wsList = wsBroker.getCloudletReceivedList();
		List<Cloudlet> appList = appBroker.getCloudletReceivedList();
		List<Cloudlet> dbList = dbBroker.getCloudletReceivedList();
		OutputWriter.printCloudletList(wsList);
		OutputWriter.printCloudletList(appList);
		OutputWriter.printCloudletList(dbList);

		// Print the debt of each user to each datacenter
		wsDatacenter.printDebts();
		appDatacenter.printDebts();
		dbDatacenter.printDebts();

		Log.printLine("StaticTier Simulation finished!");
		
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
	private static DatacenterAffinityBroker createBroker(String brokerId) {
		DatacenterAffinityBroker broker = null;
		try {
			broker = new DatacenterAffinityBroker(brokerId, 0);
			//broker = new DatacenterBroker(brokerId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}
	
}
