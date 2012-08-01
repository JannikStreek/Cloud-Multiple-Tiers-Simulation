package de.hpi_web.cloudSim.profiling;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;

import de.hpi_web.cloudSim.arx.ModelBasedPrediction;
import de.hpi_web.cloudSim.arx.NewArx;
import de.hpi_web.cloudSim.model.ResourceModelCollection;
import de.hpi_web.cloudSim.profiling.builders.DatacenterBuilder;
import de.hpi_web.cloudSim.profiling.builders.VmBuilder;
import de.hpi_web.cloudSim.profiling.datacenter.ProfilingBroker;
import de.hpi_web.cloudSim.profiling.datacenter.ProfilingVm;
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
			UtilizationThreshold bwThreshold,
			UtilizationThreshold hdThreshold,
			DatacenterBuilder dcBuilder,
			VmBuilder vmBuilder,
			Map<String, ResourceModelCollection> models,
			int vmsAtStart,
			double minPerTurn,
			double costPerMin) {
		
		Log.printLine("Starting...");
		initializeCloudSim();
		List<ProfilingBroker> brokers = prepareThreeTierScenario(observer, dcBuilder, vmBuilder, vmsAtStart, minPerTurn, costPerMin);
		
		// create a map where for each broker the CPU usage is recorded
		// if our input contains models, we will calculate the usage based on them, otherwise we take the sample file provided
		HashMap<DatacenterBroker, List<List<Double>>> layers = new HashMap<DatacenterBroker, List<List<Double>>>();
		if (models == null) {
			NewArx.init(training, running);
			layers.put(brokers.get(0), NewArx.predictWebTierUtil());
			layers.put(brokers.get(1), NewArx.predictAppTierUtil());
			layers.put(brokers.get(2), NewArx.predictDbTierUtil());
		} else {
			ModelBasedPrediction mbp = new ModelBasedPrediction(models, running);
			layers.put(brokers.get(0), mbp.predictWebTierUtil());
			layers.put(brokers.get(1), mbp.predictAppTierUtil());
			layers.put(brokers.get(2), mbp.predictDbTierUtil());
		}

		@SuppressWarnings("unused")
		UtilManager utilManager = new UtilManager(
				"UtilManager", 
				delay, 
				cpuThreshold, 
				memThreshold,
				hdThreshold,
				bwThreshold,
				layers,
				vmBuilder);
		
		CloudSim.startSimulation();
		CloudSim.stopSimulation();

		Log.printLine("Simulation finished!");
		
	}
	
	private static List<ProfilingBroker> prepareThreeTierScenario(
			Observer observer, 
			DatacenterBuilder dcBuilder, VmBuilder vmBuilder,
			int vmsAtStart,
			double minPerTurn,
			double costPerMin) {

		// setup datacenters and brokers
		Datacenter wsDatacenter = dcBuilder.setName("WebserverCenter").build();
		Datacenter appDatacenter = dcBuilder.setName("ApplicationCenter").build();
		Datacenter dbDatacenter = dcBuilder.setName("DatabaseCenter").build();
		
		ProfilingBroker wsBroker = createBroker("wsBroker", minPerTurn, costPerMin);
		ProfilingBroker appBroker = createBroker("appBroker", minPerTurn, costPerMin);
		ProfilingBroker dbBroker = createBroker("dbBroker", minPerTurn, costPerMin);
		
		wsBroker.addAffinity(wsDatacenter.getId());
		appBroker.addAffinity(appDatacenter.getId());
		dbBroker.addAffinity(dbDatacenter.getId());
		
		List<ProfilingBroker> brokers = new ArrayList<>();
		brokers.add(wsBroker);
		brokers.add(appBroker);
		brokers.add(dbBroker);
		
		List<ProfilingVm> wsVms, appVms, dbVms;
		wsVms = new ArrayList<>();
		appVms = new ArrayList<>();
		dbVms = new ArrayList<>();
		
		// create initial Vms
		for (int i = 0; i < vmsAtStart; i++) {
			wsVms.add(vmBuilder.setUserId(wsBroker.getId()).build());
			appVms.add(vmBuilder.setUserId(appBroker.getId()).build());
			dbVms.add(vmBuilder.setUserId(dbBroker.getId()).build());
		}
		
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
	private static ProfilingBroker createBroker(String brokerId, double minPerTurn, double costPerMin) {
		ProfilingBroker broker = null;
		try {
			broker = new ProfilingBroker(brokerId,minPerTurn, costPerMin);
			//broker = new DatacenterBroker(brokerId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}
	
}
