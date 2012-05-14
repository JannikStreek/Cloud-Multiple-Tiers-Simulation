package de.hpi_web.cloudSim.multitier.workload;

import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;

import de.hpi_web.cloudSim.multitier.MultiTierCloudTags;
import de.hpi_web.cloudSim.multitier.MultiTierCloudlet;
import de.hpi_web.cloudSim.multitier.staticTier.CloudletFactory;

public class SpikeWorkloadGenerator extends WorkloadGenerator {
	public static final int INTERVALL = 500;
	public static final int SPIKE_TIMEFRAME = 50;

	@Override
	void scheduleWorkloadForBroker(DatacenterBroker broker, double timeLimit) {
		int brokerId = broker.getId();
		int workload = LOAD_MIN;
		List<Cloudlet> cl = CloudletFactory.createCloudlets(0, 300, brokerId);
		double e = Math.E;
		
		for (int x = -(SPIKE_TIMEFRAME/2); x <= (SPIKE_TIMEFRAME/2); x++) {
			workload = LOAD_MIN + extraLoad(x);
			//broker.schedule(brokerId, delay, MultiTierCloudTags.REQUEST_TAG, c);
			
		}
	}

	private int extraLoad(int x) {
		int extraLoad = 0;
		extraLoad = (1/Math.sqrt(2*Math.PI);
		double a = Math.E^(-(1/2)*x^2));
		return 0;
	}

	@Override
	void scheduleWorkloadForBroker(DatacenterBroker broker) {
		// TODO Auto-generated method stub

	}

}
