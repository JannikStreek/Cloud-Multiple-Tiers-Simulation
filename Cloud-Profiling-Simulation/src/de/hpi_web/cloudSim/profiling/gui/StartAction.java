package de.hpi_web.cloudSim.profiling.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;

import de.hpi_web.cloudSim.multitier.staticTier.VmFactory;
import de.hpi_web.cloudSim.profiling.datacenter.DatacenterBuilder;
import de.hpi_web.cloudSim.profiling.datacenter.ProfilingBroker;
import de.hpi_web.cloudSim.profiling.observer.Observer;
import de.hpi_web.cloudSim.profiling.utilization.UtilManager;

public class StartAction implements ActionListener{
	
	private Observer observer;
	private int delay;
	private int upperThreshold;
	private int lowerThreshold;

	public StartAction(Observer observer) {
		super();
		this.delay = 1;
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
	
	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
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
		
		private static void start(Observer observer, int delay, int upperThreshold, int lowerThreshold) {
			
			Log.printLine("Starting...");
			initializeCloudSim();
			
			Datacenter wsDatacenter = DatacenterBuilder.createDatacenter("WebserverCenter", 0, 3);
			
			ProfilingBroker wsBroker = createBroker("wsBroker");
			wsBroker.register(observer);
			
			List<Vm> wsVms = VmFactory.createVms(0, 4, wsBroker.getId());
			
			// submit vm lists to the brokers
			wsBroker.submitVmList(wsVms);
			List<DatacenterBroker> brokers = new ArrayList<DatacenterBroker>();
			brokers.add(wsBroker);
			UtilManager utilManager = new UtilManager("UtilManager", delay, upperThreshold, lowerThreshold, brokers);

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
