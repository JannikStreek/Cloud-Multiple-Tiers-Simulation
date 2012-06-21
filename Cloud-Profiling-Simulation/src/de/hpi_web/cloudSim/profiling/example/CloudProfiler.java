package de.hpi_web.cloudSim.profiling.example;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;

import arx.NewArx;

import de.hpi_web.cloudSim.multitier.MultiTierCloudlet;
import de.hpi_web.cloudSim.multitier.staticTier.CloudletFactory;
import de.hpi_web.cloudSim.multitier.staticTier.VmFactory;
import de.hpi_web.cloudSim.profiling.datacenter.DatacenterBuilder;
import de.hpi_web.cloudSim.profiling.datacenter.ProfilingBroker;
import de.hpi_web.cloudSim.profiling.observer.Observer;
import de.hpi_web.cloudSim.profiling.utilization.UtilManager;

public class CloudProfiler {
		
	public static void start(Observer observer, double delay, String training, String running, int upperThreshold, int lowerThreshold) {
		
		Log.printLine("Starting...");
		initializeCloudSim();
		List<ProfilingBroker> brokers = prepareThreeTierScenario(observer);
		
		// create a map where for each broker the CPU usage is recorded
		NewArx.init("training.csv", "running.csv");
		HashMap<DatacenterBroker, List<List<Double>>> layers = new HashMap<DatacenterBroker, List<List<Double>>>();
		layers.put(brokers.get(0), NewArx.predictWebTierUtil());
		layers.put(brokers.get(1), NewArx.predictAppTierUtil());
		layers.put(brokers.get(2), NewArx.predictDbTierUtil());
		//List<MultiTierCloudlet> wsCloudlets = CloudletFactory.createCloudlets(0, 10, wsBroker);

		//wsBroker.submitCloudletList(wsCloudlets);

		@SuppressWarnings("unused")
		UtilManager utilManager = new UtilManager("UtilManager", delay, upperThreshold, lowerThreshold, 10000,5000,10000,5000,10000,5000,10000,5000,10000,5000, layers);
		
		CloudSim.startSimulation();
		CloudSim.stopSimulation();

		//Print results
		//List<Cloudlet> wsList = wsBroker.getCloudletReceivedList();
		Log.printLine("Simulation finished!");
		
	}
	
	private static List<ProfilingBroker> prepareThreeTierScenario(Observer observer) {

		Datacenter wsDatacenter = DatacenterBuilder.createDatacenter("WebserverCenter", 3);
		Datacenter appDatacenter = DatacenterBuilder.createDatacenter("ApplicationCenter", 3);
		Datacenter dbDatacenter = DatacenterBuilder.createDatacenter("DatabaseCenter", 3);
		
		ProfilingBroker wsBroker = createBroker("wsBroker");
		ProfilingBroker appBroker = createBroker("appBroker");
		ProfilingBroker dbBroker = createBroker("dbBroker");
		
		wsBroker.addAffinity(wsDatacenter.getId());
		appBroker.addAffinity(appDatacenter.getId());
		dbBroker.addAffinity(dbDatacenter.getId());
		
		List<ProfilingBroker> brokers = new ArrayList<ProfilingBroker>();
		brokers.add(wsBroker);
		brokers.add(appBroker);
		brokers.add(dbBroker);
		
		List<Vm> wsVms = VmFactory.createVms(1, wsBroker.getId());
		List<Vm> appVms = VmFactory.createVms(1, appBroker.getId());
		List<Vm> dbVms = VmFactory.createVms(1, dbBroker.getId());
		
		// submit vm lists to the brokers
		wsBroker.submitVmList(wsVms);
		appBroker.submitVmList(appVms);
		dbBroker.submitVmList(dbVms);
		
		// register with the observer
		wsBroker.register(observer);
		appBroker.register(observer);
		dbBroker.register(observer);
		
		return brokers;
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
	private static ProfilingBroker createBroker(String brokerId) {
		ProfilingBroker broker = null;
		try {
			broker = new ProfilingBroker(brokerId);
			//broker = new DatacenterBroker(brokerId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}
	
}
