package de.hpi_web.cloudSim.profiling.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import arx.ARX;

import de.hpi_web.cloudSim.multitier.staticTier.VmFactory;
import de.hpi_web.cloudSim.profiling.datacenter.DatacenterBuilder;
import de.hpi_web.cloudSim.profiling.datacenter.ProfilingBroker;
import de.hpi_web.cloudSim.profiling.observer.Observer;
import de.hpi_web.cloudSim.profiling.utilization.UtilManager;

public class StartAction implements ActionListener{
	
	private Observer observer;
	private double delay = 0.5;
	private int upperThreshold = 70;
	private int lowerThreshold = 30;

	public StartAction(Observer observer) {
		super();
		this.observer = observer;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		class Start extends Thread {

			@Override
			public void run() {
				SimulationStart.start(observer, delay, upperThreshold, lowerThreshold);
				
			}
			
		}
		
		Thread thread = new Start();
		thread.start();
		
		
	}
	
	public double getDelay() {
		return delay;
	}

	public void setDelay(double delay) {
		this.delay = delay;
	}
	
	public int getUpperThreshold() {
		return upperThreshold;
	}

	public void setUpperThreshold(int upperThreshold) {
		this.upperThreshold = upperThreshold;
	}

	public int getLowerThreshold() {
		return lowerThreshold;
	}

	public void setLowerThreshold(int lowerThreshold) {
		this.lowerThreshold = lowerThreshold;
	}
	
	static class SimulationStart {
		
		private static void start(Observer observer, double delay, int upperThreshold, int lowerThreshold) {
			
			Log.printLine("Starting...");
			initializeCloudSim();
			Datacenter wsDatacenter = DatacenterBuilder.createDatacenter("WebserverCenter", 0, 3);
			Datacenter appDatacenter = DatacenterBuilder.createDatacenter("ApplicationCenter", 0, 3);
			Datacenter dbDatacenter = DatacenterBuilder.createDatacenter("DatabaseCenter", 0, 3);
			
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
			
			List<Vm> wsVms = VmFactory.createVms(0, 1, wsBroker.getId());
			List<Vm> appVms = VmFactory.createVms(100, 1, appBroker.getId());
			List<Vm> dbVms = VmFactory.createVms(200, 1, dbBroker.getId());
			
			// submit vm lists to the brokers
			wsBroker.submitVmList(wsVms);
			appBroker.submitVmList(appVms);
			dbBroker.submitVmList(dbVms);
			
			// register with the observer
			wsBroker.register(observer);
			appBroker.register(observer);
			dbBroker.register(observer);
			
			// create a map where for each broker the CPU usage is recorded
			List<List<Double>> cpuValues = ARX.predictCPUUsage("training.csv", "running.csv");
			HashMap<DatacenterBroker, List<Double>> layers = new HashMap<DatacenterBroker, List<Double>>();
			int index = 1;			// we dont start at 0, this is the LoadBalancer which we do not track atm
			for (DatacenterBroker broker : brokers) {
				layers.put(broker, cpuValues.get(index));
				index++;
			}
			UtilManager utilManager = new UtilManager("UtilManager", delay, upperThreshold, lowerThreshold, layers);

			//List<MultiTierCloudlet> wsCloudlets = CloudletFactory.createCloudlets(0, 10, wsBroker);

			//wsBroker.submitCloudletList(wsCloudlets);
			
			CloudSim.startSimulation();
			CloudSim.stopSimulation();

			//Print results
			List<Cloudlet> wsList = wsBroker.getCloudletReceivedList();
			Log.printLine("Simulation finished!");
			
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


}
