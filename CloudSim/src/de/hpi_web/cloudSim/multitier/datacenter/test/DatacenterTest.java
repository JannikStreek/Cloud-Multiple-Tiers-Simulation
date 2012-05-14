package de.hpi_web.cloudSim.multitier.datacenter.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.hpi_web.cloudSim.multitier.datacenter.DatacenterAffinityBroker;
import de.hpi_web.cloudSim.multitier.example.MultiTierExample;
import de.hpi_web.cloudSim.multitier.staticTier.CloudletFactory;
import de.hpi_web.cloudSim.multitier.staticTier.DatacenterFactory;
import de.hpi_web.cloudSim.multitier.staticTier.VmFactory;

public class DatacenterTest {

	@Before
	public void setUp() throws Exception {
		
		
		
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
		
		wsBroker.setSuccessor(appBroker);
		appBroker.setSuccessor(dbBroker);
		
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

		List<Cloudlet> wsCloudlets = CloudletFactory.createCloudlets(0, 2, wsBroker.getId());
		List<Cloudlet> appCloudlets = CloudletFactory.createCloudlets(10, 5, appBroker.getId());
		List<Cloudlet> dbCloudlets = CloudletFactory.createCloudlets(20, 2, dbBroker.getId());

		wsBroker.submitCloudletList(wsCloudlets);
	}

	@After
	public void tearDown() throws Exception {
		Log.printLine("StaticTier Simulation finished!");
	}

	@Test
	public void test() {
		CloudSim.runStart();
		CloudSim.runClockTick();
		CloudSim.runClockTick();
		CloudSim.runClockTick();
		CloudSim.runClockTick();
		CloudSim.runClockTick();
		CloudSim.runClockTick();
		CloudSim.runClockTick();
		CloudSim.runClockTick();
		CloudSim.runClockTick();
		CloudSim.runClockTick();
		CloudSim.runClockTick();
		CloudSim.runClockTick();
		
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
	
	public static void initializeCloudSim() {
		int num_user = 1; // number of cloud users
		Calendar calendar = Calendar.getInstance();
		boolean trace_flag = false; // mean trace events
		
		// Initialize the CloudSim library
		CloudSim.init(num_user, calendar, trace_flag);
	}

}
