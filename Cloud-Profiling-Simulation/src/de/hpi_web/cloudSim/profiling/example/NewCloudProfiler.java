package de.hpi_web.cloudSim.profiling.example;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;

import arx.NewArx;
import de.hpi_web.cloudSim.profiling.builders.DatacenterBuilder;
import de.hpi_web.cloudSim.profiling.builders.VmBuilder;
import de.hpi_web.cloudSim.profiling.datacenter.ProfilingBroker;
import de.hpi_web.cloudSim.profiling.observer.Observer;
import de.hpi_web.cloudSim.profiling.utilization.UtilManager;
import de.hpi_web.cloudSim.profiling.utilization.UtilizationThreshold;

public class NewCloudProfiler {
		
	public static void start(
			Observer observer, 
			double delay, 
			String training, 
			String running, 
			UtilizationThreshold cpuThreshold,
			UtilizationThreshold memThreshold,
			UtilizationThreshold bwInThreshold,
			UtilizationThreshold bwOutThreshold,
			UtilizationThreshold hdReadThreshold,
			UtilizationThreshold hdWriteThreshold,
			DatacenterBuilder dcBuilder,
			VmBuilder vmBuilder) {
		
		Log.printLine("Starting...");
		initializeCloudSim();
		List<ProfilingBroker> brokers = prepareThreeTierScenario(observer, dcBuilder, vmBuilder);
		
		// create a map where for each broker the CPU usage is recorded
		NewArx.init(training, running);
		HashMap<DatacenterBroker, List<List<Double>>> layers = new HashMap<DatacenterBroker, List<List<Double>>>();
		layers.put(brokers.get(0), NewArx.predictWebTierUtil());
		layers.put(brokers.get(1), NewArx.predictAppTierUtil());
		layers.put(brokers.get(2), NewArx.predictDbTierUtil());
		//List<MultiTierCloudlet> wsCloudlets = CloudletFactory.createCloudlets(0, 10, wsBroker);

		//wsBroker.submitCloudletList(wsCloudlets);

		@SuppressWarnings("unused")
		UtilManager utilManager = new UtilManager(
				"UtilManager", 
				delay, 
				cpuThreshold, 
				memThreshold,
				hdReadThreshold,
				hdWriteThreshold,
				bwInThreshold,
				bwOutThreshold,
				layers,
				vmBuilder);
		
		CloudSim.startSimulation();
		CloudSim.stopSimulation();

		//Print results
		//List<Cloudlet> wsList = wsBroker.getCloudletReceivedList();
		Log.printLine("Simulation finished!");
		
	}
	
	private static List<ProfilingBroker> prepareThreeTierScenario(
			Observer observer, 
			DatacenterBuilder dcBuilder, VmBuilder vmBuilder) {

		Datacenter wsDatacenter = dcBuilder.setName("WebserverCenter").build();
		Datacenter appDatacenter = dcBuilder.setName("ApplicationCenter").build();
		Datacenter dbDatacenter = dcBuilder.setName("DatabaseCenter").build();
		
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
		
		List<Vm> wsVms, appVms, dbVms;
		wsVms = new ArrayList<Vm>();
		appVms = new ArrayList<Vm>();
		dbVms = new ArrayList<Vm>();
		
		// TODO: include different amount of start Vms (has to be passed down from the GUI)
		wsVms.add(vmBuilder.setUserId(wsBroker.getId()).build());
		appVms.add(vmBuilder.setUserId(appBroker.getId()).build());
		dbVms.add(vmBuilder.setUserId(dbBroker.getId()).build());
		
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
